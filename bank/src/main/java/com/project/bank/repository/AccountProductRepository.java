package com.project.bank.repository;

import com.project.bank.entity.Account;
import com.project.bank.entity.AccountProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AccountProductRepository extends JpaRepository<AccountProduct,String>, QuerydslPredicateExecutor<AccountProduct> {

}
