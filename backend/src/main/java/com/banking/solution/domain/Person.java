package com.banking.solution.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@MappedSuperclass
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    protected String name;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    protected String gender;

    @Min(0)
    @Max(130)
    @Column(nullable = false)
    protected Integer age;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    protected String identification;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    protected String address;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    protected String phone;
}