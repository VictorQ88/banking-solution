package com.banking.solution.service;

import java.time.LocalDate;

import com.banking.solution.dto.AccountStatementReportDTO;

public interface ReportService {

    AccountStatementReportDTO accountStatement(
            Long clientId,
            LocalDate from,
            LocalDate to
    );
}
