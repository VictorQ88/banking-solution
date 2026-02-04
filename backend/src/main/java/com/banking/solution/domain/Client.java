package com.banking.solution.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
public class Client extends Person {

    @NotBlank
    @Size(max = 40)
    @Column(name = "client_id", nullable = false, length = 40)
    private String clientId;

    @NotBlank
    @Size(min = 4, max = 120)
    @Column(nullable = false, length = 120)
    private String password;

    @NotNull
    @Column(nullable = false)
    private Boolean active;

    public Client(
            String name,
            String gender,
            Integer age,
            String identification,
            String address,
            String phone,
            String clientId,
            String password,
            Boolean active
    ) {
        super(null, name, gender, age, identification, address, phone);
        this.clientId = clientId;
        this.password = password;
        this.active = active;
    }
}
