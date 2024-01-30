package com.project.bank.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanDTO {  //대출 상품 뿌려주기

    private String bankName; // 은행명

    private String product_name; //대출 상품

    private String loanMoney; // 대출금액

    private String interest; //이자율



}
