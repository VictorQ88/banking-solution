package com.banking.solution.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.solution.dto.AccountStatementReportDTO;
import com.banking.solution.service.ReportService;

@RestController
@RequestMapping("/reportes")
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @GetMapping
    public AccountStatementReportDTO accountStatement(
            @RequestParam Long clientId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        return service.accountStatement(clientId, from, to);
    }
}
