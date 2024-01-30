package com.project.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@Table(name="savings")
@AllArgsConstructor
@NoArgsConstructor
public class Savings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SavingsNo;

    private boolean isActive;

    private int savingsBalance; //현재까지 적금된 금액

    private int returnAmount; // 적금 반환액

    private int missedPayments; // 미납 횟수

    private int earnedInterest; //반환액

    private String savingsStartDate; // 적금 가입 날짜

    private String savingsEndDate; //적금이 끝나는 날짜


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "savingsProductId")
    private SavingsProduct savingsProduct;   //적금 상품


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountNumber")
    private Account account;   //적금 게좌


    @OneToMany(mappedBy = "savings", cascade = CascadeType.ALL)
    private List<SavingsHistory> histories;


    public void addHistory(SavingsHistory savingsHistory){
        this.histories.add(savingsHistory);
    }

    public void EndSavings(int returnAmount, int earnedInterest ){
        this.isActive = false;
        this.returnAmount = returnAmount;
        this.earnedInterest = earnedInterest;
    }

    public void UpdateSavingsBalance(int savingsBalance){
        this.savingsBalance += savingsBalance;

    }

    public void UpdateMissedPayments(){
        this.missedPayments = this.missedPayments + 1;
    }





}
