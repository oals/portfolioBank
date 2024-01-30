package com.project.bank.repository;

import com.project.bank.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface HistoryRepository extends JpaRepository<History,Long>, QuerydslPredicateExecutor<History> {
}
