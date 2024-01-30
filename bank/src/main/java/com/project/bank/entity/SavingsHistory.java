package com.project.bank.entity;


import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name="Savings_history")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SavingsHistory {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long savingsId;

    private Boolean savingsPaymentStatus; // 이번 달 적금 처리 상태

    private String savingsStartDate;   // 적금 한 날짜

    private int savingsCount; // 적금 만기까지의 카운트


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SavingsNo")
    private Savings savings;


}
