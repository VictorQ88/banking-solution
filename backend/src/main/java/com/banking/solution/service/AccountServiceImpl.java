package com.banking.solution.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.solution.domain.Account;
import com.banking.solution.domain.Client;
import com.banking.solution.dto.AccountDTO;
import com.banking.solution.exception.NotFoundException;
import com.banking.solution.repository.AccountRepository;
import com.banking.solution.repository.ClientRepository;
import com.banking.solution.utils.AccountMapper;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            ClientRepository clientRepository
    ) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
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
    @Transactional
    public AccountDTO create(AccountDTO dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new NotFoundException("Client not found"));

        Account entity = AccountMapper.toEntity(dto);
        entity.setId(null);
        entity.setClient(client);

        Account saved = accountRepository.save(entity);
        return AccountMapper.toDTO(saved);
    }

    @Override
    public AccountDTO update(Long id, AccountDTO dto) {
        Account existing = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new NotFoundException("Client not found"));

        existing.setAccountNumber(dto.getAccountNumber());
        existing.setAccountType(dto.getAccountType());
        existing.setInitialBalance(dto.getInitialBalance());
        existing.setActive(dto.getActive());
        existing.setClient(client);

        Account saved = accountRepository.save(existing);
        return AccountMapper.toDTO(saved);
    }

    @Override
    public void delete(Long id) {
        Account existing = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        accountRepository.delete(existing);
    }
}
