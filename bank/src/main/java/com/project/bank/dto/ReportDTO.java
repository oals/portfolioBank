package com.project.bank.dto;

import com.project.bank.config.Role;
import com.project.bank.entity.Member;
import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ReportDTO {
    private Long reportNum;
    private String siteName; // 사이트 명
    private String scamProductType; // 사기 물품 종류
    private String scamProductName; // 사기 물품명
    private String suspectId; // 용의자 아이디
    private String scamPostUrl; // 사기 게시글 url
    private String suspectBankName; // 용의자 은행명
    private String suspectFullName; // 용의자 성명
    private String suspectAccountNumber; // 용의자 계좌번호
    private String depositAmount; // 입금금액
    private String depositDate; // 입금일
    private String suspectContact; // 용의자 연락처
    private String suspectGender; // 용의자 성별
    private String suspectCharacteristics; // 용의자 특징
    private String statementContent; // 진술서 내용
    private String evidencePhotos; // 증거 사진
    private String reporterDescription; // 신고자 설명
    private String reporterContact; // 신고자 연락처
    private String reporterEmail; // 신고자 이메일

    private boolean reportStatus;    //검토 여부

    private boolean agreementToTheTerms;
    private boolean agreementToTheTerms2;
    private String reportDate;


    private String memberId;
    private String memberRole;
}
