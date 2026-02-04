package com.banking.solution.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientDTO {

    private Long id;

    @NotBlank
    @Size(max = 120)
    private String name;

    @NotBlank
    @Size(max = 20)
    private String gender;

    @Min(18)
    @Max(130)
    private Integer age;

    @NotBlank
    @Size(max = 20)
    private String identification;

    @NotBlank
    @Size(max = 200)
    private String address;

    @NotBlank
    @Size(max = 20)
    private String phone;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String clientId;

    @NotBlank
    @Size(min = 4, max = 120)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    private Boolean active;
}
