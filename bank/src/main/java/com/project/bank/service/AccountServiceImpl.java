package com.project.bank.service;

import com.project.bank.dto.*;
import com.project.bank.entity.*;

import com.project.bank.repository.AccountProductRepository;
import com.project.bank.repository.AccountRepository;
import com.project.bank.repository.HistoryRepository;
import com.project.bank.repository.MemberRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class AccountServiceImpl implements AccountService{

    @PersistenceContext
    EntityManager em;



    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final HistoryRepository historyRepository;
    private  final MemberRepository memberRepository;
    private final HistoryService historyService;

    private final AccountProductRepository accountProductRepository;
    @Override
    public void accountRegister(AccountDTO accountDTO) {

        Member member = Member.builder()
                .memberId(accountDTO.getMemberId())
                .build();

        AccountProduct accountProduct = AccountProduct.builder()
                .accountProductName(accountDTO.getAccountProductName())
                .build();

        log.info("검사" + accountProduct.getAccountProductName());

        Random rand = new Random();
        StringBuilder accountNumber = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            int digit = rand.nextInt(10); // 0-9 사이의 숫자를 무작위로 선택
            accountNumber.append(digit);
        }


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        String createDate = now.format(formatter);

        //비밀번호 암호화




        Account account = Account.builder()
                .accountNumber(AccountNumberUpdate(accountNumber.toString()))
                .accountPswd(accountDTO.getAccountPswd())
                .createDate(createDate)
                .balance(0)
                .member(member)
                .accountProduct(accountProduct)
                .build();


        accountRepository.save(account);

    }

    @Override

    public boolean CheckReceiveAccount(TransferDTO transferDTO) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QAccount qAccount = QAccount.account;

        // 받는 사람의 계좌 정보 검사

        boolean chk = queryFactory.selectFrom(qAccount)
                .where(qAccount.accountProduct.accountProductName.eq(transferDTO.getReceiveAccountName())
                        .and(qAccount.accountNumber.eq(transferDTO.getReceiveAccountNumber()))
                        .and(qAccount.member.memberName.eq(transferDTO.getReceiveMemberName())))
                .fetchOne() == null ? false : true;


        return chk;

    }

    @Override
    public boolean CheckSendAccount(TransferDTO transferDTO) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QAccount qAccount = QAccount.account;

        Account account = accountRepository.findById(transferDTO.getSendAccountNumber()).orElseThrow();

        boolean sendChk = false;

        if(account.getBalance() < transferDTO.getSendBalance()){
            sendChk = false;
        }else{
            sendChk = true;
        }




        return sendChk;
    }

    @Override
    public List<AccountProductListDTO> AccountProductList() {

        List<AccountProduct> accountProducts = accountProductRepository.findAll();

        List<AccountProductListDTO> accountProductDTOS = new ArrayList<>();

        for(int i = 0; i < accountProducts.size(); i++){

            AccountProductListDTO accountProductListDTO = AccountProductListDTO.builder()
                    .accountProductName(accountProducts.get(i).getAccountProductName())
                    .productIntroduction(accountProducts.get(i).getProductIntroduction())
                    .build();

            accountProductDTOS.add(accountProductListDTO);
        }


        return accountProductDTOS;
    }

    @Override
    public AccountProductDTO AccountProductInfo(String accountProductName) {

        AccountProduct accountProduct = accountProductRepository.findById(accountProductName).orElseThrow();

        AccountProductDTO accountProductDTO = AccountProductDTO.builder()
                .accountProductName(accountProduct.getAccountProductName())
                .additionalServices(accountProduct.getAdditionalServices())
                .productIntroduction(accountProduct.getProductIntroduction())
                .eligibleCustomers(accountProduct.getEligibleCustomers())
                .additionalServices(accountProduct.getAdditionalServices())
                .features(accountProduct.getAccountProductName())
                .interestPayment(accountProduct.getInterestPayment())
                .depositorProtection(accountProduct.getDepositorProtection())
                .interestRate(accountProduct.getInterestRate())
                .build();


        return accountProductDTO;
    }

    @Override
    public boolean SendMoney(TransferDTO transferDTO) {

        boolean result = true;
        //보내는 사람의 예금 감소
        Account SendAccount = accountRepository.findById(transferDTO.getSendAccountNumber()).orElseThrow();

        //받는 사람의 예금 추가
        Account ReceiveAccount = accountRepository.findById(transferDTO.getReceiveAccountNumber()).orElseThrow();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        String date = now.format(formatter);


        try{

        // 보내는 사람의 계좌 이체 내역 추가
            History SendHistory = History.builder()
                    .balance(SendAccount.getBalance() - transferDTO.getSendBalance())  //현재 잔액
                    .money(transferDTO.getSendBalance())                                //보낸 금액
                    .opt("송금")
                    .counterpartyName(ReceiveAccount.getMember().getMemberName())       //받는 사람 이름
                    .counterpartyAccountNumber(ReceiveAccount.getAccountNumber())           //받는 사람의 계좌 정보
                    .counterpartyAccountName(ReceiveAccount.getAccountProduct().getAccountProductName())
                    .updateDate(date)                                    //해당 날짜
                    .account(SendAccount)
                    .build();

            SendAccount.addHistory(SendHistory);




        //받는 사람의 계좌 이체 내역 추가

        History ReceiveHistory = History.builder()
                    .balance(ReceiveAccount.getBalance() + transferDTO.getSendBalance())
                    .money(transferDTO.getSendBalance())
                    .opt("입금")
                    .counterpartyAccountNumber(SendAccount.getAccountNumber())
                    .counterpartyName(SendAccount.getMember().getMemberName())
                    .counterpartyAccountName(SendAccount.getAccountProduct().getAccountProductName())
                    .updateDate(date)
                    .account(ReceiveAccount)
                    .build();

            ReceiveAccount.addHistory(ReceiveHistory);



            SendAccount.MinusBalance(transferDTO.getSendBalance());
            accountRepository.save(SendAccount);


            ReceiveAccount.PlusBalance(transferDTO.getSendBalance());
            accountRepository.save(ReceiveAccount);

        }catch (Exception e){
            e.printStackTrace();
            result =false;
        }


        return result;
    }

    @Override
    public boolean AccountPswdCheck(String accountNumber, String accountPswd) {


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QAccount qAccount = QAccount.account;


        boolean chk = queryFactory.selectFrom(qAccount)
                .where(qAccount.accountNumber.eq(accountNumber).and(qAccount.accountPswd.eq(accountPswd)))
                .fetchOne() == null ? false : true;

        return chk;
    }

    @Override
    public boolean accountProductRegisterChk(String memberId, String accountProductName) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QAccount qAccount = QAccount.account;


        boolean chk = queryFactory.selectFrom(qAccount)
                .where(qAccount.member.memberId.eq(memberId)
                        .and(qAccount.accountProduct.accountProductName.eq(accountProductName))).fetch().isEmpty();


        return chk;
    }


    @Override
    public AccountDTO SelectAccountInfo(String accountNumber) {

        Account account = accountRepository.findById(accountNumber).orElseThrow();

        AccountDTO accountDTO = AccountDTO.builder()
                .accountProductName(account.getAccountProduct().getAccountProductName())
                .createDate(account.getCreateDate())
                .memberId(account.getMember().getMemberId())
                .balance(account.getBalance())
                .createDate(account.getCreateDate())
                .accountNumber(account.getAccountNumber())
                .build();

        return accountDTO;
    }


    @Override
    public List<AccountDTO> SelectAccount(String memberId,String accountNumber) {
//

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QAccount qAccount = QAccount.account;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(accountNumber == null){
            booleanBuilder.and(qAccount.member.memberId.eq(memberId));
        }else{
            booleanBuilder.and(qAccount.member.memberId.eq(memberId)
                    .and(qAccount.accountNumber.ne(accountNumber)));
        }

        List<Account> query =  queryFactory.selectFrom(qAccount)
                .where(booleanBuilder)
                .fetch();


        List<AccountDTO> list = new ArrayList<>();


        for(int i = 0; i < query.size(); i++){
            AccountDTO accountDTO = AccountDTO.builder()
                    .accountProductName(query.get(i).getAccountProduct().getAccountProductName())
                    .accountNumber(query.get(i).getAccountNumber())
                    .balance(query.get(i).getBalance())
                    .memberId(query.get(i).getMember().getMemberId())
                    .createDate(query.get(i).getCreateDate())
                    .build();


            list.add(accountDTO);

        }



        return list;





    }



}
