package com.project.bank.dto;


import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountProductDTO {

    private String accountProductName;
    private String productIntroduction; // 상품 소개
    private String eligibleCustomers; // 가입대상
    private String interestRate; // 이율
    private String interestPayment; // 이자지급
    private String additionalServices; // 부가서비스
    private String features; // 특징
    private String depositorProtection; // 예금자 보호여부


}
