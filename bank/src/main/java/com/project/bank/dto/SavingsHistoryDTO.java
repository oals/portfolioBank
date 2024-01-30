package com.project.bank.dto;

import com.project.bank.entity.Account;
import com.project.bank.entity.SavingsProduct;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SavingsHistoryDTO {

    private Long savingsId;

    private int savingsBalance; //현재까지 적금된 금액
    private Boolean savingsPaymentStatus; // 적금 처리 상태

    private String savingsStartDate;   // 적금 시작일자
    private String savingsEndDate; //적금이 끝나는 날짜
    private int savingsCount; // 적금 만기까지의 카운트
    private Long savingsProductId;   //적금 상품
    private String accountNumber;   //적금 게좌



}
