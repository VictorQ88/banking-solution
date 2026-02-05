package com.banking.solution.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.solution.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByClientId(Long clientId);
}
