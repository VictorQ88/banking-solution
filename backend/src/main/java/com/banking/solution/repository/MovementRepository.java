package com.banking.solution.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.banking.solution.domain.Account;
import com.banking.solution.domain.Movement;

public interface MovementRepository extends JpaRepository<Movement, Long> {

    @Query("SELECT m FROM Movement m WHERE m.account = :account ORDER BY m.movementDate DESC LIMIT 1")
    Optional<Movement> findLastMovementByAccount(@Param("account") Account account);

    @Query("SELECT m FROM Movement m WHERE m.account.id = :accountId ORDER BY m.movementDate DESC")
    List<Movement> findByAccountId(@Param("accountId") Long accountId);

    @Query("""
            SELECT m
            FROM Movement m
            JOIN m.account a
            JOIN a.client c
            WHERE c.id = :clientId
            AND m.movementDate BETWEEN :from AND :to
            ORDER BY a.id, m.movementDate
            """)
    List<Movement> report(
            @Param("clientId") Long clientId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
