package com.project.bank.RestController;

import com.project.bank.service.MemberService;
import com.project.bank.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequiredArgsConstructor
@Log4j2
public class MemberRestController {

    private final MemberService memberService;
    private final SmsService smsService;

    @GetMapping("/memberIdCheck")
    public boolean memberIdCheck(String memberId){

        boolean chk = false;

        chk = memberService.MemberIdCheck(memberId);
        log.info("검사 결과 : " + chk);


        return chk;
    }


    @GetMapping("/sendSMS")
    public String sendSMS(String phoneNumber){
        log.info(phoneNumber);


       return smsService.SendSms(phoneNumber);

    }







}
