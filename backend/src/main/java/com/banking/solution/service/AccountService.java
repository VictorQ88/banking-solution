package com.banking.solution.service;

import java.util.List;

import com.banking.solution.dto.AccountDTO;
import com.banking.solution.dto.AccountUpdateDTO;

public interface AccountService {
    List<AccountDTO> findAll();
    AccountDTO findById(Long id);
    AccountDTO create(AccountDTO dto);
    AccountDTO update(Long id, AccountUpdateDTO dto);
    void delete(Long id);
}