package com.project.bank.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountProductListDTO {

    private String accountProductName;
    private String productIntroduction; // 상품 소개

}
