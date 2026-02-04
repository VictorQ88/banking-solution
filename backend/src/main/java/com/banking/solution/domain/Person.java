package com.banking.solution.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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