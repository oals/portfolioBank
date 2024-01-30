package com.project.bank.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import java.util.List;

@Getter
@Table(name="account")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {

    @Id
    @Column(name="account_number")
    private String accountNumber;       // 계좌 번호


    @Column(name="account_pswd")
    private String accountPswd;         //계좌 비밀번호

    @Column(name="create_date")
    private String createDate;          // 생성 일자

    @Column(name="balance")
    private int balance;                //계좌 잔액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;              // 회원 아이디


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountProductNo")
    private AccountProduct accountProduct;


    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<History> histories;


    public void addHistory(History history){
        this.histories.add(history);
    }


    public void MinusBalance(int balance){
        this.balance -= balance;

    }

    public void PlusBalance(int balance){
        this.balance += balance;

    }





}
