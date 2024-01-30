package com.project.bank.service;

import com.project.bank.entity.*;
import com.project.bank.repository.AccountRepository;
import com.project.bank.repository.HistoryRepository;
import com.project.bank.repository.SavingsRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@Log4j2
@RequiredArgsConstructor
public class ThreadServiceImpl implements ThreadService{


    private final SavingsRepository savingsRepository;
    private final AccountRepository accountRepository;

    private final HistoryRepository historyRepository;

    @PersistenceContext
    EntityManager em;

    @Override
    @Transactional
    public boolean savingsThread(String accountNumber,String savingsProductName) {


        boolean threadChk = true;

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QSavings qSavings = QSavings.savings;

        Savings savingsHistoryEntity = queryFactory.selectFrom(qSavings)
                .where(qSavings.account.accountNumber.eq(accountNumber)
                        .and(qSavings.savingsProduct.productName.eq(savingsProductName)
                                .and(qSavings.isActive.eq(true))))
                .limit(1L)
                .fetchOne();

        //만기 체크
        int savingCount = savingsHistoryEntity.getHistories().size() + 1;



        log.info("savingCount : " +  savingCount);


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        String date = now.format(formatter);

        //스레드 종료
        if(savingCount == savingsHistoryEntity.getSavingsProduct().getMaturityPeriod() + 1){

            double principal = savingsHistoryEntity.getSavingsBalance(); // 원금
            double monthlyInterestRate = savingsHistoryEntity.getSavingsProduct().getAppliedInterestRate(); // 월간 이율
            int termInMonths = savingsHistoryEntity.getSavingsProduct().getMaturityPeriod(); // 기간 (개월)

            // 이율 변환
            monthlyInterestRate = monthlyInterestRate / 12; // 연간 이율을 월간 이율로 변환
            int returnSavings = (int)(principal * Math.pow((1 + monthlyInterestRate / 100), termInMonths));


            Account account = savingsHistoryEntity.getAccount();

            History history = History.builder()
                    .balance(account.getBalance() +  returnSavings)  //현재 잔액
                    .money(returnSavings)                      //보낸 금액
                    .opt("적금 반환")
                    .counterpartyName(savingsHistoryEntity.getSavingsProduct().getProductName())
                    .account(savingsHistoryEntity.getAccount())
                    .updateDate(date)                                    //해당 날짜
                    .build();

            account.PlusBalance(returnSavings);

            savingsHistoryEntity.EndSavings(returnSavings,returnSavings - (int)principal);


            accountRepository.save(account);
            historyRepository.save(history);
            savingsRepository.save(savingsHistoryEntity);

            log.info("만기 반환액 : " + returnSavings);

            threadChk = false;


        } else{  //만기 상태가 아닐 시



            if(savingsHistoryEntity.getAccount().getBalance() >= savingsHistoryEntity.getSavingsProduct().getMonthlyDepositAmount()){

                int accountBalance = savingsHistoryEntity.getAccount().getBalance();
                //계좌에서 금액 감소
                savingsHistoryEntity.getAccount().MinusBalance(savingsHistoryEntity.getSavingsProduct().getMonthlyDepositAmount());


                log.info("savingsHistoryEntity.getHistories() : " + savingsHistoryEntity.getHistories().size());
                log.info("savingcount : " + savingCount);
                //적금 데이터 저장
                SavingsHistory savingsHistory = SavingsHistory.builder()
                        .savingsStartDate(date)
                        .savingsPaymentStatus(true)
                        .savingsCount(savingCount)
                        .savings(savingsHistoryEntity)
                        .build();

                savingsHistoryEntity.addHistory(savingsHistory);
                savingsHistoryEntity.UpdateSavingsBalance(savingsHistoryEntity.getSavingsProduct().getMonthlyDepositAmount());

                //일반 내역에 저장
                History history = History.builder()
                        .balance(accountBalance -  savingsHistoryEntity.getSavingsProduct().getMonthlyDepositAmount())  //현재 잔액
                        .money(savingsHistoryEntity.getSavingsProduct().getMonthlyDepositAmount())                      //보낸 금액
                        .opt("적금")
                        .counterpartyName(savingsHistoryEntity.getSavingsProduct().getProductName())
                        .account(savingsHistoryEntity.getAccount())
                        .updateDate(date)                                    //해당 날짜
                        .build();

                accountRepository.save(savingsHistoryEntity.getAccount());
                savingsRepository.save(savingsHistoryEntity);
                historyRepository.save(history);


            }else{
                //계좌 잔고가 부족 할때 미납 처리
                SavingsHistory savingsHistory = SavingsHistory.builder()
                        .savingsStartDate(date)
                        .savingsPaymentStatus(false)
                        .savingsCount(savingCount)
                        .savings(savingsHistoryEntity)
                        .build();

                savingsHistoryEntity.addHistory(savingsHistory);
                savingsHistoryEntity.UpdateMissedPayments();

                savingsRepository.save(savingsHistoryEntity);


            }
        }


        return threadChk;
    }

}
