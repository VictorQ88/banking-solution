package com.banking.solution.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.banking.solution.domain.Account;
import com.banking.solution.domain.Client;
import com.banking.solution.dto.AccountDTO;
import com.banking.solution.exception.NotFoundException;
import com.banking.solution.repository.AccountRepository;
import com.banking.solution.repository.ClientRepository;
import com.banking.solution.utils.AccountMapper;
import com.banking.solution.utils.AccountNumberGenerator;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final AccountNumberGenerator accountNumberGenerator;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            ClientRepository clientRepository,
            AccountNumberGenerator accountNumberGenerator
    ) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.accountNumberGenerator = accountNumberGenerator;
    }

    @Override
    public List<AccountDTO> findAll() {
        return accountRepository.findAll()
                .stream()
                .map(AccountMapper::toDTO)
                .toList();
    }

    @Override
    public AccountDTO findById(Long id) {
        Account a = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        return AccountMapper.toDTO(a);
    }

    @Override
    public AccountDTO create(AccountDTO dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new NotFoundException("Client not found"));

        Account entity = AccountMapper.toEntity(dto);
        entity.setId(null);
        entity.setClient(client);
        entity.setAccountNumber(accountNumberGenerator.generate());

        log.info("Creating account accountNumber={}, clientId={}",
                dto.getAccountNumber(), dto.getClientId());

        Account saved = accountRepository.save(entity);

        log.info("Account created id={}, accountNumber={}, clientId={}",
                saved.getId(),
                saved.getAccountNumber(),
                saved.getClient().getId());

        return AccountMapper.toDTO(saved);
    }

    @Override
    public AccountDTO update(Long id, AccountDTO dto) {

        log.info("Updating account id={}", id);

        Account existing = accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Account not found id={}", id);
                    return new NotFoundException("Account not found");
                });

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new NotFoundException("Client not found"));

        existing.setAccountNumber(dto.getAccountNumber());
        existing.setAccountType(dto.getAccountType());
        existing.setInitialBalance(dto.getInitialBalance());
        existing.setActive(dto.getActive());
        existing.setClient(client);

        Account saved = accountRepository.save(existing);

        log.info("Account updated id={}, accountNumber={}",
                saved.getId(), saved.getAccountNumber());

        return AccountMapper.toDTO(saved);
    }

    @Override
    public void delete(Long id) {
        accountRepository.findById(id)
                .ifPresentOrElse(
                        accountRepository::delete,
                        () -> {
                            throw new NotFoundException("Account not found");
                        }
                );
    }
}
