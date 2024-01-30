package com.project.bank.RestController;


import com.project.bank.config.Role;
import com.project.bank.dto.PageRequestDTO;
import com.project.bank.dto.PageResponseDTO;
import com.project.bank.dto.ReportDTO;
import com.project.bank.dto.ReportListDTO;
import com.project.bank.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Log4j2
@RequiredArgsConstructor
public class AccountReportRestController {

    private final ReportService reportService;



    @GetMapping("/reportAccountList")
    public PageResponseDTO reportAccountList(PageRequestDTO pageRequestDTO){

        PageResponseDTO<ReportListDTO> pageResponseDTO = reportService.reportAccountList(pageRequestDTO);

        return pageResponseDTO;

    }



    @GetMapping("/searchAccountNumber")
    public PageResponseDTO<ReportListDTO> searchAccountNumber(PageRequestDTO pageRequestDTO, String suspectAccountNumber){



        PageResponseDTO<ReportListDTO>  list = reportService.searchAccount(pageRequestDTO,suspectAccountNumber);


        return list;
    }


    @PutMapping("/registrationReport")
    public void registrationReport(Long reportNum){

        reportService.registrationReport(reportNum);

    }

    @DeleteMapping("/deleteReport")
    public void deleteReport(Long reportNum){

        reportService.deleteReport(reportNum);

    }




    @GetMapping("/reportAccountDetailView")
    public ReportDTO reportAccountDetailView(Long reportNum){

        ReportDTO reportDTO = reportService.reportAccountDetailView(reportNum);



        return reportDTO;
    }




}
