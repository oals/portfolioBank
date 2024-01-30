package com.project.bank.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
@Log4j2
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService{
    @Override
    public String SendSms(String phoneNumber) {

        String newPhoneNumber = phoneNumber.replaceAll("-","");
        Message coolsms = new Message("api_key", "api_secret");

        Random rand = new Random();
        String randNum = "";

        randNum = rand.nextInt((9999 - 1000) + 1) + 1000 + "";


        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("to", newPhoneNumber);
            params.put("from", "01090465953");
            params.put("type", "sms");
            params.put("text", "bank 회원가입 인증번호는 [" + randNum + "] 입니다.");

            coolsms.send(params); // 메시지 전송

        } catch (CoolsmsException e) {
            throw new RuntimeException(e);
        }


        return randNum;

    }
}
