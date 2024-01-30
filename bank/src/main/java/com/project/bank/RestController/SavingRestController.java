package com.project.bank.RestController;

import com.project.bank.dto.PageRequestDTO;
import com.project.bank.dto.PageResponseDTO;
import com.project.bank.dto.SavingsDTO;
import com.project.bank.service.AccountService;
import com.project.bank.service.SavingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Log4j2
@RequiredArgsConstructor
public class SavingRestController {

    private final SavingsService savingsService;

    private final AccountService accountService;



    @GetMapping("/savingsDTOList")
    public PageResponseDTO savingsDTOList(PageRequestDTO pageRequestDTO,String accountNumber,boolean isActive){

        PageResponseDTO<SavingsDTO> savingsDTOPageResponseDTO =  savingsService.getMySavings(pageRequestDTO,accountNumber,isActive);

        return savingsDTOPageResponseDTO;
    }

    @GetMapping("/SavingsCheckBalance")
    public boolean SavingsCheckBalance(String accountNumber, String productName){

        //해당 계좌의 잔액 검사 (1회분 검사)
        boolean balacneChk = savingsService.SavingsCheckBalance(accountNumber,productName);

//


        return balacneChk;
    }


    @PostMapping("/SavingsRegister")
    public boolean SavingsRegister(String accountNumber, String productName){

            log.info("신규 가입 접근 ");
         boolean chk = savingsService.NewSavings(accountNumber,productName);

         return chk;
    }


}
