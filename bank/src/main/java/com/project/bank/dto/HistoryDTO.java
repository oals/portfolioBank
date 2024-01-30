package com.project.bank.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.bank.entity.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HistoryDTO {

    private Long Id;
    private int balance;               // 계좌 잔액
    private int money;               // 상대방 이체 금액

    private String  updateDate;   // 입출금 일자


    private String counterpartyName;          // 입출금자
    private String counterpartyAccountNumber;  // 조회 계좌의 주인

    private String counterpartyAccountName;

    private String opt;              // 입/출금/이자 여부
    private String accountNumber;            // 내 계좌 번호

}
