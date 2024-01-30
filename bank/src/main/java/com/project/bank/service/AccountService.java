package com.project.bank.service;

import com.project.bank.dto.*;
import com.project.bank.entity.Account;
import com.project.bank.entity.AccountProduct;
import com.project.bank.entity.Member;
import lombok.extern.log4j.Log4j2;
import org.thymeleaf.expression.Arrays;

import java.time.LocalDateTime;
import java.util.List;


public interface AccountService {


    default String AccountNumberUpdate(String accountNumber){

        //4개 단위로 4개 나누기 후에 추가 후 합치기
        String[] str = new String[4];

        for(int i = 0; i < 4; i++){
            str[i] = accountNumber.substring(i * 4 , (i + 1) * 4);
        }
        String NewAccountNumber = "";

        for(int i = 0; i < 4; i++){

            if(i != 3) {
                NewAccountNumber += str[i] + "-";
            }else{
                NewAccountNumber += str[i];
            }
        }

        return NewAccountNumber;
    }




     void accountRegister(AccountDTO accountDTO); //계좌 생성


     boolean CheckReceiveAccount(TransferDTO transferDTO); //계좌 검사

     boolean CheckSendAccount(TransferDTO transferDTO);

    List<AccountProductListDTO> AccountProductList();

    AccountProductDTO AccountProductInfo(String accountProductName);


     boolean SendMoney(TransferDTO transferDTO); //이체

     boolean AccountPswdCheck(String accountNumber,String accountPswd);


    boolean accountProductRegisterChk(String memberId,String accountProductName);

     AccountDTO SelectAccountInfo(String accountNumber); // 계좌 번호로 계좌 정보 얻기

     List<AccountDTO> SelectAccount(String memberId,String accountNumber);







}
