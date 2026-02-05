package com.banking.solution.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDTO {

    private Long id;

    private String accountNumber;

    @NotBlank
    @Size(max = 20)
    private String accountType;

    @NotNull
    private BigDecimal initialBalance;

    @NotNull
    private Boolean active;

    @NotNull
    private Long clientId;
}
