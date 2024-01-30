package com.project.bank.repository;

import com.project.bank.entity.SavingsProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsProductRepository extends JpaRepository<SavingsProduct,Long>, QuerydslPredicateExecutor<SavingsProduct> {
}
