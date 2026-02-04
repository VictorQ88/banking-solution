package com.banking.solution.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.solution.domain.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * find client by identification.
     */
    Optional<Client> findByIdentification(String identification);
}
