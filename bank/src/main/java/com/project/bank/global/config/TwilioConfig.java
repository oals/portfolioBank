package com.project.bank.global.config;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class TwilioConfig {
    private String accountSid;
    private String authToken;

    @PostConstruct
    public void init() {
        accountSid = "ACf6b36b883955c267122a69a82535c8a8";
        authToken = "2a6586aa5a452797da0e8dcd695fb5d1";
        Twilio.init(accountSid, authToken);
    }
}