package com.banking.solution.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.banking.solution.dto.MovementDTO;
import com.banking.solution.service.MovementService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/movements")
public class MovementController {

    private final MovementService service;

    public MovementController(MovementService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovementDTO create(@Valid @RequestBody MovementDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<MovementDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public MovementDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/account/{accountId}")
    public List<MovementDTO> findByAccount(@PathVariable Long accountId) {
        return service.findByAccountId(accountId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
