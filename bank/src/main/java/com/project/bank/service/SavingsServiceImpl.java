package com.project.bank.service;

import com.project.bank.dto.*;
import com.project.bank.entity.*;
import com.project.bank.repository.AccountRepository;
import com.project.bank.repository.HistoryRepository;
import com.project.bank.repository.SavingsProductRepository;
import com.project.bank.repository.SavingsRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class SavingsServiceImpl implements SavingsService{
    
    private final SavingsRepository savingsRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final HistoryService historyService;
    private final HistoryRepository historyRepository;

    private final SavingsProductRepository savingsProductRepository;

    private final ThreadService threadService;

    @PersistenceContext
    EntityManager em;




    public class MyRunnable implements Runnable {
        private final String accountNumber;
        private final String savingsProductName;
        private boolean threadChk;

        public MyRunnable(String accountNumber,String savingsProductName) {
            this.accountNumber = accountNumber;
            this.savingsProductName =  savingsProductName;
            this.threadChk = true;
        }

        @Override
        public void run() {
            while(threadChk) {

                try {//30L * 24 * 60 * 60 * 1000
                    Thread.sleep(60000);

                    threadChk = threadService.savingsThread(accountNumber,savingsProductName);



                }    catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



    @Override
    public boolean SavingsCheckBalance(String accountNumber, String productName) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QAccount qAccount = QAccount.account;
        QSavingsProduct qSavingsProduct = QSavingsProduct.savingsProduct;

        boolean chk =  queryFactory.select(qAccount.balance.gt(
                        queryFactory.select(qSavingsProduct.monthlyDepositAmount).from(qSavingsProduct)
                                .where(qSavingsProduct.productName.eq(productName))
                ))
                .from(qAccount)
                .where(qAccount.accountNumber.eq(accountNumber))
                .fetchOne();



        return chk;
    }





    @Override
    public boolean NewSavings(String accountNumber, String productName) {  //적금 가입

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QSavingsProduct qSavingsProduct = QSavingsProduct.savingsProduct;

        boolean chk = false;
        try{

            SavingsProduct savingsProduct = queryFactory.selectFrom(qSavingsProduct)
                    .where(qSavingsProduct.productName.eq(productName))
                    .fetchOne();


            Account SendAccount = accountRepository.findById(accountNumber).orElseThrow();

            int accountBalance = SendAccount.getBalance();

            //계좌에서 금액 감소
            SendAccount.MinusBalance(savingsProduct.getMonthlyDepositAmount());



            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
            String startDate = now.format(formatter);


            LocalDateTime now2 = LocalDateTime.now().plusMonths(savingsProduct.getMaturityPeriod());
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
            String endDate = now2.format(formatter2);

            Savings savings = Savings.builder()
                    .savingsProduct(savingsProduct)
                    .isActive(true)
                    .savingsBalance(savingsProduct.getMonthlyDepositAmount())
                    .account(SendAccount)
                    .histories(new ArrayList<>())
                    .savingsStartDate(startDate)
                    .savingsEndDate(endDate)
                    .build();


            //적금 데이터 저장
            SavingsHistory savingsHistory = SavingsHistory.builder()
                    .savingsStartDate(startDate)
                    .savingsPaymentStatus(true)
                    .savingsCount(1)
                    .savings(savings)
                    .build();


            savings.addHistory(savingsHistory);



            //일반 내역에 저장
            History history = History.builder()
                    .balance(accountBalance -  savingsProduct.getMonthlyDepositAmount())  //현재 잔액
                    .money(savingsProduct.getMonthlyDepositAmount())                      //보낸 금액
                    .opt("적금")
                    .counterpartyName(savingsProduct.getProductName())
                    .account(SendAccount)
                    .updateDate(startDate)                                    //해당 날짜
                    .build();


            accountRepository.save(SendAccount);
            savingsRepository.save(savings);
            historyRepository.save(history);

            chk = true;

            String sendAccountNumber = SendAccount.getAccountNumber();
            String savingsProductName = savings.getSavingsProduct().getProductName();
            Thread thread = new Thread(new MyRunnable(sendAccountNumber,savingsProductName));
            thread.start(); // 스레드를 실행합니다.


        }catch (Exception e){
            e.printStackTrace();
            chk=false;
        }



        return chk;
    }

    @Override
    public PageResponseDTO<SavingsDTO> getMySavings(PageRequestDTO pageRequestDTO, String accountNumber, boolean isActive) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QSavings qSavings = QSavings.savings;

        Pageable pageable = pageRequestDTO.getPageable();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(isActive){
            booleanBuilder.and(qSavings.isActive.eq(true)
                    .and(qSavings.account.accountNumber.eq(accountNumber)));
        }else{
            booleanBuilder.and(qSavings.isActive.eq(false)
                    .and(qSavings.account.accountNumber.eq(accountNumber)));
        }

        List<Savings> query =  queryFactory.selectFrom(qSavings)
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qSavings.SavingsNo.desc())
                .fetch();

        List<SavingsDTO> list = new ArrayList<>();

        for(int i = 0; i < query.size(); i++){
            SavingsDTO savingsDTO = SavingsDTO.builder()
                    .accountNumber(query.get(i).getAccount().getAccountNumber())
                    .savingsStartDate(query.get(i).getSavingsStartDate())
                    .savingsEndDate(query.get(i).getSavingsEndDate())
                    .earnedInterest(query.get(i).getEarnedInterest())
                    .missedPayments(query.get(i).getMissedPayments())
                    .savingsBalance(query.get(i).getSavingsBalance())
                    .savingsProductName(query.get(i).getSavingsProduct().getProductName())
                    .returnAmount(query.get(i).getReturnAmount())
                    .build();

            list.add(savingsDTO);

        }


        Long count = queryFactory
                .select(qSavings.count())
                .from(qSavings)
                .where(booleanBuilder)
                .fetchOne();



        return PageResponseDTO.<SavingsDTO>widthAll()
                .list(list)
                .pageRequestDTO(pageRequestDTO)
                .total(Integer.parseInt(count.toString()))
                .build();

    }

    @Override
    public boolean ChkSavings(String memberId, String savingsProductName) {

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
        QSavings qSavings = QSavings.savings;

        boolean chk = jpaQueryFactory.selectFrom(qSavings)
                .where(qSavings.savingsProduct.productName.eq(savingsProductName)
                        .and(qSavings.isActive.eq(true))
                        .and(qSavings.account.member.memberId.eq(memberId)))
                .fetch().isEmpty();

        log.info("해당 적금 상품 가입 여부 : " + chk);

        return chk;
    }


    @Override
    public List<SavingsProductDTO> selectSavingsProductList() {




        List<SavingsProduct> query =  savingsProductRepository.findAll();

        List<SavingsProductDTO> dtoList = new ArrayList<>();

        for(int i = 0; i < query.size(); i++){
            SavingsProductDTO savingsProductDTO = SavingsProductDTO.builder()
                    .savingsProductId(query.get(i).getSavingsProductId())
                    .productName(query.get(i).getProductName())
                    .interestRate(query.get(i).getInterestRate())
                    .maturityPeriod(query.get(i).getMaturityPeriod())
                    .productIntro(query.get(i).getProductIntro())
                    .eligibilityCriteria(query.get(i).getEligibilityCriteria())
                    .governmentSupport(query.get(i).getGovernmentSupport())
                    .monthlyDepositAmount(query.get(i).getMonthlyDepositAmount())
                    .maturityAmount(query.get(i).getMaturityAmount())
                    .build();

            dtoList.add(savingsProductDTO);

        }

        return dtoList;


    }

    @Override
    public SavingsProductDTO selectSavingsProductDetailView(Long savingsProductId) {

        SavingsProduct savingsProduct = savingsProductRepository.findById(savingsProductId).orElseThrow();



        SavingsProductDTO savingsProductDTO = modelMapper.map(savingsProduct,SavingsProductDTO.class);






        return savingsProductDTO;
    }


}
