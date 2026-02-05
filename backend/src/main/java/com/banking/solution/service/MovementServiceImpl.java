package com.banking.solution.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.banking.solution.domain.Account;
import com.banking.solution.domain.Movement;
import com.banking.solution.dto.MovementDTO;
import com.banking.solution.exception.BusinessException;
import com.banking.solution.exception.NotFoundException;
import com.banking.solution.repository.AccountRepository;
import com.banking.solution.repository.MovementRepository;
import com.banking.solution.utils.MovementMapper;

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

        log.info("Creating movement type={}, value={}, accountId={}",
                dto.getMovementType(), dto.getValue(), dto.getAccountId());

        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        BigDecimal value = dto.getValue() == null ? BigDecimal.ZERO : dto.getValue();
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Value must be greater than zero");
        }

        String type = dto.getMovementType() == null ? "" : dto.getMovementType().trim().toUpperCase();

        if (!"DEPOSIT".equals(type) && !"WITHDRAWAL".equals(type)) {
            throw new IllegalArgumentException("Invalid movement type. Must be DEPOSIT or WITHDRAWAL");
        }

        BigDecimal currentBalance = movementRepository.findLastMovementByAccount(account)
                .map(Movement::getBalance)
                .orElse(BigDecimal.ZERO);

        BigDecimal actualValue = "DEPOSIT".equals(type) ? value : value.negate();

        BigDecimal newBalance = currentBalance.add(actualValue);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            log.warn("Insufficient balance accountId={}, currentBalance={}, withdrawal={}",
                    account.getId(), currentBalance, value);
            throw new BusinessException("Insufficient balance");
        }

        Movement movement = new Movement();
        movement.setId(null);
        movement.setMovementDate(LocalDateTime.now());
        movement.setMovementType(type);
        movement.setValue(actualValue);
        movement.setBalance(newBalance);
        movement.setAccount(account);

        Movement saved = movementRepository.save(movement);

        log.info("Movement created id={}, type={}, value={}, balance={}, accountId={}",
                saved.getId(), saved.getMovementType(), saved.getValue(), saved.getBalance(), account.getId());

        return MovementMapper.toDTO(saved);
    }

    @Override
    public List<MovementDTO> findAll() {
        return movementRepository.findAll()
                .stream()
                .map(MovementMapper::toDTO)
                .toList();
    }

    @Override
    public MovementDTO findById(Long id) {
        Movement m = movementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movement not found"));
        return MovementMapper.toDTO(m);
    }

    @Override
    public List<MovementDTO> findByAccountId(Long accountId) {
        log.info("Fetching movements for accountId={}", accountId);

        accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        return movementRepository.findByAccountId(accountId)
                .stream()
                .map(MovementMapper::toDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        movementRepository.findById(id)
                .ifPresentOrElse(
                        movementRepository::delete,
                        () -> {
                            throw new NotFoundException("Movement not found");
                        }
                );
    }
}
