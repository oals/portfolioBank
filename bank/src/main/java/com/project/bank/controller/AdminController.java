package com.project.bank.controller;


import com.project.bank.dto.PageRequestDTO;
import com.project.bank.dto.PageResponseDTO;
import com.project.bank.dto.ReportListDTO;
import com.project.bank.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
public class AdminController {

    private final ReportService reportService;

    @GetMapping("/adminPage")
    public String adminPage(){




        return "admin/adminPage";
    }

    @GetMapping("/AccountReportCheckPage")
    public String AccountReportCheckPage(PageRequestDTO pageRequestDTO, Model model){


        PageResponseDTO<ReportListDTO> pageResponseDTO = reportService.reportAccountList(pageRequestDTO);

        model.addAttribute("reportListDTO",pageResponseDTO);


        return "admin/AccountReportCheckPage";
    }

}
