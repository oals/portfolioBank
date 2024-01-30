package com.project.bank.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportListDTO {

    private Long reportNum;
    private String siteName; // 사이트 명
    private String scamProductType; // 사기 물품 종류
    private String depositAmount; // 입금금액
    private String reportDate; // 신고일

}
