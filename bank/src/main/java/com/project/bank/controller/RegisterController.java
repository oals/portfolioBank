package com.project.bank.controller;


import com.project.bank.dto.AccountDTO;
import com.project.bank.dto.MemberDTO;
import com.project.bank.dto.PageRequestDTO;
import com.project.bank.dto.PageResponseDTO;
import com.project.bank.entity.Account;
import com.project.bank.entity.Member;
import com.project.bank.service.AccountService;
import com.project.bank.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class RegisterController {

    private final MemberService memberService;
    private final AccountService accountService;


    @GetMapping(value="/registerPage")
    public String registerPage(){

        log.info("register go ...");

        return "register/registerPage";
    }






    @PostMapping(value="/register")   //회원가입 및 계좌 생성 페이지로 전송
    public String register(MemberDTO memberDTO, Model model){


        log.info("가입 정보 : "+memberDTO);

        //회원 정보 저장
        memberService.register(memberDTO);



        return "index";

    }







}
