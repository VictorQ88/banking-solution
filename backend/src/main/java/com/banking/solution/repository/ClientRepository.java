package com.banking.solution.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.solution.domain.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
