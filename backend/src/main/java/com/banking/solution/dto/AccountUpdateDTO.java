package com.banking.solution.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountUpdateDTO {
    @NotNull
    private Boolean active;

}
 