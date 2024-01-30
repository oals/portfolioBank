package com.project.bank.service;

import com.project.bank.config.Role;
import com.project.bank.dto.PageRequestDTO;
import com.project.bank.dto.PageResponseDTO;
import com.project.bank.dto.ReportDTO;
import com.project.bank.dto.ReportListDTO;
import com.project.bank.entity.Member;
import com.project.bank.entity.QAccount;
import com.project.bank.entity.QReport;
import com.project.bank.entity.Report;
import com.project.bank.repository.ReportRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{


    @PersistenceContext
    EntityManager em;

    private final ModelMapper modelMapper;

    private final ReportRepository reportRepository;

    @Override
    public void report(ReportDTO reportDTO) {

        Member member = Member.builder()
                .memberId(reportDTO.getMemberId())
                .build();

        log.info("신고자 아이디 : " + reportDTO.getMemberId());

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String reportDate = now.format(formatter);


        Report report = Report.builder()
                .siteName(reportDTO.getSiteName())
                .scamProductType(reportDTO.getScamProductType())
                .scamProductName(reportDTO.getScamProductName())
                .suspectId(reportDTO.getSuspectId())
                .scamPostUrl(reportDTO.getScamPostUrl())
                .suspectBankName(reportDTO.getSuspectBankName())
                .suspectFullName(reportDTO.getSuspectFullName())
                .suspectAccountNumber(reportDTO.getSuspectAccountNumber())
                .depositAmount(reportDTO.getDepositAmount())
                .depositDate(reportDTO.getDepositDate())
                .suspectContact(reportDTO.getSuspectContact())
                .suspectGender(reportDTO.getSuspectGender())
                .suspectCharacteristics(reportDTO.getSuspectCharacteristics())
                .statementContent(reportDTO.getStatementContent())
                .evidencePhotos(reportDTO.getEvidencePhotos())
                .reporterDescription(reportDTO.getReporterDescription())
                .reporterContact(reportDTO.getReporterContact())
                .reporterEmail(reportDTO.getReporterEmail())
                .agreementToTheTerms(reportDTO.isAgreementToTheTerms())
                .agreementToTheTerms2(reportDTO.isAgreementToTheTerms2())
                .reportDate(reportDate)
                .reportStatus(false)
                .member(member)
                .build();

                reportRepository.save(report);
    }

    @Override
    public PageResponseDTO<ReportListDTO> searchAccount(PageRequestDTO pageRequestDTO, String suspectAccountNumber) {


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QReport qReport = QReport.report;


        Pageable pageable = pageRequestDTO.getPageable();

        List<Report> query = queryFactory.selectFrom(qReport)
                .where(qReport.suspectAccountNumber.eq(suspectAccountNumber)
                        .and(qReport.reportStatus.eq(true)))
                .orderBy(qReport.reportDate.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();


        List<ReportListDTO> list = new ArrayList<>();

        for(int i = 0; i < query.size(); i++){

            ReportListDTO reportListDTO = ReportListDTO.builder()
                    .reportNum(query.get(i).getReportNum())
                    .siteName(query.get(i).getSiteName())
                    .scamProductType(query.get(i).getScamProductType())
                    .depositAmount(query.get(i).getDepositAmount())
                    .reportDate(query.get(i).getReportDate())
                    .build();





            list.add(reportListDTO);

        }

        Long count = queryFactory.selectFrom(qReport)
                .where(qReport.suspectAccountNumber.eq(suspectAccountNumber)
                        .and(qReport.reportStatus.eq(true)))
                .stream().count();




        return PageResponseDTO.<ReportListDTO>widthAll()
                .pageRequestDTO(pageRequestDTO)
                .list(list)
                .total( count.intValue())
                .build();



    }

    @Override
    public PageResponseDTO<ReportListDTO> reportAccountList(PageRequestDTO pageRequestDTO) {


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QReport qReport = QReport.report;


        Pageable pageable = pageRequestDTO.getPageable();

        List<Report> query = queryFactory.selectFrom(qReport)
                .where(qReport.reportStatus.eq(false))
                .orderBy(qReport.reportDate.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();


        List<ReportListDTO> list = new ArrayList<>();

        for(int i = 0; i < query.size(); i++){

            ReportListDTO reportListDTO = ReportListDTO.builder()
                    .reportNum(query.get(i).getReportNum())
                    .siteName(query.get(i).getSiteName())
                    .scamProductType(query.get(i).getScamProductType())
                    .depositAmount(query.get(i).getDepositAmount())
                    .reportDate(query.get(i).getReportDate())
                    .build();

            list.add(reportListDTO);

        }

        Long count = queryFactory.selectFrom(qReport)
                .where(qReport.reportStatus.eq(false))
                .stream().count();


        return PageResponseDTO.<ReportListDTO>widthAll()
                .pageRequestDTO(pageRequestDTO)
                .list(list)
                .total( count.intValue())
                .build();


    }

    @Override
    public ReportDTO reportAccountDetailView(Long reportNum) {

        Report report = reportRepository.findById(reportNum).orElseThrow();


        ReportDTO reportDTO = modelMapper.map(report,ReportDTO.class);
        reportDTO.setMemberId(report.getMember().getMemberId());
        reportDTO.setMemberRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());

        if(!SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains("ADMIN")){
            reportDTO.setReporterContact("010-****-****");
            reportDTO.setReporterDescription("***");
            reportDTO.setReporterEmail("****@*****.com");
        }



        return reportDTO;
    }

    @Override
    public void registrationReport(Long reportNum) {

        Report report = reportRepository.findById(reportNum).orElseThrow();

        report.setReportStatus(true);

        reportRepository.save(report);

    }

    @Override
    public void deleteReport(Long reportNum) {

        reportRepository.deleteById(reportNum);

    }
}
