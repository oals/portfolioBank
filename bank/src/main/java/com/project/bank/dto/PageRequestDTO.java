package com.project.bank.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 5;

    // 메서드

    // 페이징 구성을 설정(보여질 페이지번호, 페이지당 레코드수, 정렬여부)
    public Pageable getPageable(){

        return PageRequest.of(this.page-1, this.size);
    }


}
