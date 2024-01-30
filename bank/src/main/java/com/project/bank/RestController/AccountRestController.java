package com.project.bank.RestController;

import com.project.bank.dto.*;
import com.project.bank.entity.Account;

import com.project.bank.entity.AccountProduct;
import com.project.bank.entity.SavingsHistory;
import com.project.bank.service.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class AccountRestController {

    private final AccountService accountService;
    private final HistoryService historyService;






    @GetMapping("/AccountPswdChk")
    public boolean AccountPswdChk(String accountNumber,String accountPswd){

        log.info(accountNumber);
        log.info(accountPswd);
        boolean result = false;

        result = accountService.AccountPswdCheck(accountNumber,accountPswd);


        return result;
    }


    @GetMapping("/accountProductRegisterChk")
    public boolean accountProductRegisterChk(Principal principal, String accountProductName){

        boolean chk =  accountService.accountProductRegisterChk(principal.getName(),accountProductName);

        return chk;
    }

    @GetMapping("/AccountProductInfo")
    public AccountProductDTO AccountProductInfo(String accountProductName){

       AccountProductDTO accountProductDTO = accountService.AccountProductInfo(accountProductName);

       return accountProductDTO;
    }





    @GetMapping("/sendCheck")
    public String sendCheck(TransferDTO transferDTO){

        log.info(transferDTO);

        String CheckMessage = "";

        //상대방 계좌 존재 검사
        boolean receiveChk = accountService.CheckReceiveAccount(transferDTO);

        if(receiveChk) {
            //계좌 비밀번호 검사
            boolean sendPswdChk = accountService.AccountPswdCheck(transferDTO.getSendAccountNumber(), transferDTO.getSendAccountPswd());

            if(sendPswdChk) {
                //계좌 잔액 검사
                boolean sendMoneyChk = accountService.CheckSendAccount(transferDTO);
                if(!sendMoneyChk){
                    CheckMessage = "계좌의 잔액이 부족합니다";
                }else{
                    CheckMessage = "정상";
                }
            }else{
                CheckMessage = "계좌의 비밀번호를 다시 확인해주세요.";
            }
        }else{
            CheckMessage = "계좌 정보를 다시 확인해주세요.";
        }

        return CheckMessage;
    }




    @PutMapping("/UpdateAccount")
    public boolean InsertMoney(TransferDTO transferDTO){

        log.info("이체 정보 : " + transferDTO);

        //
        boolean chk =  accountService.SendMoney(transferDTO);


        return chk;

    }




    @GetMapping("/GetAccount")
    public List<AccountDTO> GetAccount(Principal principal,String accountNumber){

        List<AccountDTO> list = accountService.SelectAccount(principal.getName(),accountNumber);


      return list;
    }






    @GetMapping("/GetOptAccountInfo")
    public PageResponseDTO<HistoryDTO> GetAccountInfo(String accountNumber,String opt,PageRequestDTO pageRequestDTO){

        log.info("계좌번호 : "+accountNumber);
        log.info("옵션 : " + opt);


        PageResponseDTO<HistoryDTO> list=  historyService.SelectHistory(accountNumber,opt,pageRequestDTO);


        return list;
    }




}
