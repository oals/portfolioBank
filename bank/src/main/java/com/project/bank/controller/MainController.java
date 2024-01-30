package com.project.bank.controller;

import com.project.bank.dto.*;
import com.project.bank.service.AccountService;
import com.project.bank.service.MemberService;
import com.project.bank.service.SavingsService;
import groovy.util.logging.Log4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MainController {


    private final AccountService accountService;
    private final MemberService memberService;
    private final SavingsService savingsService;


    @GetMapping("/")
    public String main(Model model,Principal principal ,String ErrorMessage){

        if (principal != null)  {
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains("ADMIN")) {
                model.addAttribute("adminChk", "true");
            }else{
                model.addAttribute("adminChk", "false");
            }
        }else{
            model.addAttribute("adminChk", "false");
        }

        if(ErrorMessage != null){
            model.addAttribute("ErrorMessage", true);
        }


        return "index";
    }



}
