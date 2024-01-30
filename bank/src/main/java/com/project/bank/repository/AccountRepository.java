package com.project.bank.repository;

import com.project.bank.entity.Account;
import com.project.bank.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,String>, QuerydslPredicateExecutor<Account> {


}
