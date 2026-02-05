package com.banking.solution.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.solution.domain.Movement;

public interface MovementRepository extends JpaRepository<Movement, Long> {

}
