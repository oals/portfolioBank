package com.project.bank.controller;

import com.project.bank.dto.SavingsProductDTO;
import com.project.bank.dto.SavingsProductListDTO;
import com.project.bank.entity.SavingsProduct;
import com.project.bank.service.SavingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class SavingController {

    private final SavingsService savingsService;

    @GetMapping("/SavingPage")
    public String SavingPage(Model model){

        List<SavingsProductDTO> savingsProductDTOS =  savingsService.selectSavingsProductList();

        model.addAttribute("savingsProductDTOS",savingsProductDTOS);
        

        return "saving/savingProductPage";
    }


    @GetMapping("/savingProductDetailViewPage")
    public String savingProductDetailViewPage(Long savingsProductId, Model model, Principal principal){

        SavingsProductDTO savingsProductDTO = savingsService.selectSavingsProductDetailView(savingsProductId);

        List<SavingsProductDTO> savingsProductDTOS =  savingsService.selectSavingsProductList();


        if (principal != null)  {
            boolean savingsJoinChk = savingsService.ChkSavings(principal.getName(),savingsProductDTO.getProductName());
            model.addAttribute("savingsJoinChk",savingsJoinChk);
        }else{
            model.addAttribute("savingsJoinChk",false);
        }




        model.addAttribute("savingsProductDTO",savingsProductDTO);
        model.addAttribute("savingsProductDTOS",savingsProductDTOS);

        return "saving/savingProductDetailViewPage";
    }



}
