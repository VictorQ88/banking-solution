package com.banking.solution.utils;

import com.banking.solution.domain.Account;
import com.banking.solution.dto.AccountDTO;

public final class AccountMapper {

    private AccountMapper() {
    }

    public static AccountDTO toDTO(Account a) {
        if (a == null) {
            return null;
        }

        return new AccountDTO(
                a.getId(),
                a.getAccountNumber(),
                a.getAccountType(),
                a.getInitialBalance(),
                a.getActive(),
                a.getClient() != null ? a.getClient().getId() : null
        );
    }

    public static Account toEntity(AccountDTO dto) {
        if (dto == null) {
            return null;
        }

        Account a = new Account();
        a.setId(dto.getId());
        a.setAccountNumber(dto.getAccountNumber());
        a.setAccountType(dto.getAccountType());
        a.setInitialBalance(dto.getInitialBalance());
        a.setActive(dto.getActive());
        return a;
    }
}
