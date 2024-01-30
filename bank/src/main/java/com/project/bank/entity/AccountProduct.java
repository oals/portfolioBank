package com.project.bank.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name="account_product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AccountProduct {

    @Id
    private String accountProductName;
     private String productIntroduction; // 상품 소개
     private String eligibleCustomers; // 가입대상
     private String interestRate; // 이율
     private String interestPayment; // 이자지급
     private String additionalServices; // 부가서비스
     private String features; // 특징
     private String depositorProtection; // 예금자 보호여부


}
