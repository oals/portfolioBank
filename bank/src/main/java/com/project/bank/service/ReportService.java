package com.project.bank.service;

import com.project.bank.dto.PageRequestDTO;
import com.project.bank.dto.PageResponseDTO;
import com.project.bank.dto.ReportDTO;
import com.project.bank.dto.ReportListDTO;

import java.util.List;

public interface ReportService {


    void report(ReportDTO reportDTO);


    PageResponseDTO<ReportListDTO> searchAccount(PageRequestDTO pageRequestDTO, String suspectAccountNumber);


    PageResponseDTO<ReportListDTO> reportAccountList(PageRequestDTO pageRequestDTO);


    ReportDTO reportAccountDetailView(Long reportNum);

    void registrationReport(Long reportNum);

    void deleteReport(Long reportNum);
}
