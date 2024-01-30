package com.project.bank.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SavingsProductListDTO {

    private Long savingsProductId;

    private String productName; // 적금 상품의 이름
    private double interestRate; // 적금 상품의 이자율
    private String subscriptionPeriod; // 가입기간





}
