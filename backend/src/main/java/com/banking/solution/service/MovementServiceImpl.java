package com.banking.solution.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.banking.solution.dto.MovementDTO;
import com.banking.solution.repository.AccountRepository;
import com.banking.solution.repository.MovementRepository;

@Service
public class MovementServiceImpl implements MovementService {

    private static final Logger log = LoggerFactory.getLogger(MovementServiceImpl.class);

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;

    public MovementServiceImpl(
            MovementRepository movementRepository,
            AccountRepository accountRepository
    ) {
        this.movementRepository = movementRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public MovementDTO create(MovementDTO dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MovementDTO> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MovementDTO findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MovementDTO> findByAccountId(Long accountId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
