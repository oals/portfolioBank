package com.project.bank.repository;

import com.project.bank.entity.Account;
import com.project.bank.entity.Savings;
import com.project.bank.entity.SavingsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SavingsRepository extends JpaRepository<Savings,Long>, QuerydslPredicateExecutor<Savings> {




;
}
