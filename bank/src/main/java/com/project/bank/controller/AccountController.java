package com.project.bank.controller;

import com.project.bank.dto.AccountDTO;
import com.project.bank.dto.HistoryDTO;
import com.project.bank.dto.TransferDTO;
import com.project.bank.service.AccountService;
import com.project.bank.service.HistoryService;
import com.project.bank.dto.*;
import com.project.bank.entity.AccountProduct;
import com.project.bank.service.AccountService;
import com.project.bank.service.HistoryService;
import com.project.bank.service.ReportService;
import com.project.bank.service.SavingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.expression.Arrays;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.expression.Arrays;

import java.security.Principal;
import java.sql.Array;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class AccountController {

    private final HistoryService historyService;
    private final AccountService accountService;

    private final SavingsService savingsService;
    private final ReportService reportService;
    @GetMapping("/CreateAccount")
    public String CreateAccount(Model model){
        
        log.info("계좌 등록 페이지 접근");
       List<AccountProductListDTO> accountProductListDTOS =  accountService.AccountProductList();

        model.addAttribute("accountProductList",accountProductListDTOS);


        return "register/accountRegisterPage";
    }
    @GetMapping("/accountSearch")
    public String accountSearch(){

        return "account/accountSearch";
    }

    @GetMapping("/accountReport")
    public String accountReport(){

        return "account/accountReport";
    }


    @PostMapping("/accountRegister")  //계좌 생성
    public ModelAndView accountRegister(AccountDTO accountDTO, Principal principal){


        accountDTO.setMemberId(principal.getName());

        log.info("검사2 : "+ accountDTO.getAccountProductName());
        //계좌 생성
        accountService.accountRegister(accountDTO);

        //컨트롤러 -> 컨트롤러 이동 코드
        ModelAndView MAV = new ModelAndView();
        MAV.setViewName("redirect:/");
        return MAV;

    }


    @PostMapping("/reportAccount")
    public ModelAndView reportAccount(ReportDTO reportDTO,Principal principal){

        reportDTO.setMemberId(principal.getName());
        reportService.report(reportDTO);


        ModelAndView MAV = new ModelAndView();
        MAV.setViewName("redirect:/");
        return MAV;

    }





    @GetMapping("/accountInfo")
    public String accountInfo(String accountNumber,Model model,PageRequestDTO pageRequestDTO){
        log.info("계좌 정보 페이지 접근");

        //계좌 정보 / 내역 가져오기
        PageResponseDTO<HistoryDTO> historyDTO = historyService.SelectHistory(accountNumber,"전체",pageRequestDTO);

        PageResponseDTO<HistoryDTO> savingsHistoryDTOS = historyService.SelectHistory(accountNumber,"적금",pageRequestDTO);

        PageResponseDTO<SavingsDTO> savingsDTOTrueList = savingsService.getMySavings(pageRequestDTO,accountNumber,true);

        PageResponseDTO<SavingsDTO> savingsDTOFalseList = savingsService.getMySavings(pageRequestDTO,accountNumber,false);



        AccountDTO accountDTO = accountService.SelectAccountInfo(accountNumber);

       log.info(historyDTO);

       model.addAttribute("accountDTO",accountDTO);
       model.addAttribute("historyDTO",historyDTO);
       model.addAttribute("savingsHistoryDTO",savingsHistoryDTOS);
       model.addAttribute("savingsDTOTrueList",savingsDTOTrueList);
       model.addAttribute("savingsDTOFalseList",savingsDTOFalseList);

        return "account/accountInfo";
    }



    @GetMapping("/sendPage")
    public String sendPage(Model model,String accountNumber){
        //보내는 사람 정보
        AccountDTO accountDTO = accountService.SelectAccountInfo(accountNumber);
        List<AccountProductListDTO> accountProductListDTOS =  accountService.AccountProductList();

        model.addAttribute("accountProductList",accountProductListDTOS);
        model.addAttribute("accountDTO",accountDTO);

        return "account/sendMoney";
    }





}
