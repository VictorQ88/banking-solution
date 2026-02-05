package com.banking.solution.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportAccountDTO {

    private String accountNumber;
    private String accountType;
    private BigDecimal initialBalance;
    private BigDecimal totalCredits;
    private BigDecimal totalDebits;
    private BigDecimal finalBalance;
    private List<MovementDTO> movements;
}
