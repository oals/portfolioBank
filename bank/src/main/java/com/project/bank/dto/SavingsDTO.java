package com.project.bank.dto;

import com.project.bank.entity.Account;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import com.project.bank.entity.SavingsProduct;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SavingsDTO {

    private Long savingsNo;

    private int returnAmount; // 적금 반환액

    private int missedPayments; // 미납 횟수

    private int earnedInterest; //이율

    private int savingsBalance; //적금액

    private String savingsStartDate; // 적금 가입 날짜

    private String savingsEndDate; //적금이 끝나는 날짜

    private String savingsProductName;   //적금 상품

    private String accountNumber;   //적금 게좌


}
