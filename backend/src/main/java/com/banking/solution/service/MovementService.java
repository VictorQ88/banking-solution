package com.banking.solution.service;

import java.util.List;

import com.banking.solution.dto.MovementDTO;

public interface MovementService {

    MovementDTO create(MovementDTO dto);

    List<MovementDTO> findAll();

    MovementDTO findById(Long id);

    List<MovementDTO> findByAccountId(Long accountId);

    void delete(Long id);
}
