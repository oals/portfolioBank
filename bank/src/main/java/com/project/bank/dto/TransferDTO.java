package com.project.bank.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransferDTO {

    //보내는 사람

    private String sendAccountNumber;
    private String sendAccountName;

    private String sendAccountPswd;
    private String sendMemberName;
    private int sendBalance;

    //받는 사람
    private String receiveAccountNumber;
    private String receiveAccountName;
    private String receiveMemberName;



}
