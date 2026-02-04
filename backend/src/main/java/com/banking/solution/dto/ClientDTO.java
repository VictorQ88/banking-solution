package com.banking.solution.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ClientDTO {

    @NotBlank
    @Size(max = 120)
    public String name;

    @NotBlank
    @Size(max = 20)
    public String gender;

    @Min(18)
    @Max(130)
    public Integer age;

    @NotBlank
    @Size(max = 20)
    public String identification;

    @NotBlank
    @Size(max = 200)
    public String address;

    @NotBlank
    @Size(max = 20)
    public String phone;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String clientId;

    @NotBlank
    @Size(min = 4, max = 120)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String password;

    @NotNull
    public Boolean active;
}
