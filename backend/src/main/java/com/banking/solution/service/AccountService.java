package com.banking.solution.service;

import java.util.List;

import com.banking.solution.dto.AccountDTO;

public interface AccountService {
    List<AccountDTO> findAll();
    AccountDTO findById(Long id);
    AccountDTO create(AccountDTO dto);
    AccountDTO update(Long id, AccountDTO dto);
    void delete(Long id);
}