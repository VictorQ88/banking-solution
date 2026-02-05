package com.banking.solution.utils;

import com.banking.solution.domain.Account;
import com.banking.solution.domain.Movement;
import com.banking.solution.dto.MovementDTO;

public final class MovementMapper {

    private MovementMapper() {
    }

    public static MovementDTO toDTO(Movement m) {
        return new MovementDTO(
                m.getId(),
                m.getMovementDate(),
                m.getMovementType(),
                m.getValue(),
                m.getBalance(),
                m.getAccount() != null ? m.getAccount().getId() : null
        );
    }

    public static Movement toEntity(MovementDTO dto) {
        Movement m = new Movement();
        m.setId(dto.getId());
        m.setMovementType(dto.getMovementType());
        m.setValue(dto.getValue());
        return m;
    }

    public static void setAccount(Movement m, Account a) {
        m.setAccount(a);
    }
}
