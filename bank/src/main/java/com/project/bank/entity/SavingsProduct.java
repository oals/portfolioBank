package com.project.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SavingsProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long savingsProductId;
    private String productName; // 적금 상품의 이름
    private double interestRate; // 적금 상품의 이자율
    private int maturityPeriod; // 적금 계약의 만기 기간
    private String productIntro; // 적금 상품의 소개
    private String eligibilityCriteria; // 적금 상품의 가입 조건
    private double governmentSupport; // 정부 지원금의 정보
    private int monthlyDepositAmount; // 사용자가 매월 적금에 납입하는 금액
    private int maturityAmount; // 적금 계약이 만료될 때 받게 되는 총 금액

    private String productRating; // 상품평점
    private String consultationRequest; // 상담신청
    private String detailedDepositRate; // 예금금리를 상세히 알아보세요
    private String monthlyDepositAmountBased; // 월납입금액기준 월납입액 원 목표기간 개월 예금금리 %
    private String maturityPaymentAmountBased; // 만기지급액기준 가입금액

    private String subscriptionPeriod; // 가입기간
    private double subscriptionAmount; // 가입금액
    private String depositBusiness; // 납입방법
    private String subscriptionMethod; // 가입방법
    private String requiredDocuments; // 필요서류
    private String taxBenefits; // 세제혜택
    private double principalOrInterest; // 원금 또는 이자
    private String paymentRestrictions; // 지급제한
    private double appliedInterestRate; // 적용금리
    private String interestPaymentMethod; // 이자지급방식





}
