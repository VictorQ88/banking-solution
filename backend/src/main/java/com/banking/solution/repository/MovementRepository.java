package com.banking.solution.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.banking.solution.domain.Account;
import com.banking.solution.domain.Movement;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    
    @Query("SELECT m FROM Movement m WHERE m.account = :account ORDER BY m.movementDate DESC LIMIT 1")
    Optional<Movement> findLastMovementByAccount(@Param("account") Account account);
}
