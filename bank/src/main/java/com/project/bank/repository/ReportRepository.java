package com.project.bank.repository;

import com.project.bank.entity.Member;
import com.project.bank.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ReportRepository extends JpaRepository<Report, Long>, QuerydslPredicateExecutor<Report> {
}
