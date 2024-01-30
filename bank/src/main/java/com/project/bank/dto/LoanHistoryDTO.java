package com.project.bank.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanHistoryDTO { //대출 내역 조회

    private String bankName;   //은행명
    private String memberName; // 유저이름
    private String productName; // 상품명
    private String loanMoney; // 대출금액
    private String interest; //이자
    private LocalDateTime loanDate; // 대출날짜
    private LocalDateTime redemptionDate; // 상환일

}
