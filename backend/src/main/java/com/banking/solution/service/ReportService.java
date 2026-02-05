package com.banking.solution.service;

import java.time.LocalDateTime;

import com.banking.solution.dto.AccountStatementReportDTO;

public interface ReportService {

    AccountStatementReportDTO accountStatement(
            Long clientId,
            LocalDateTime from,
            LocalDateTime to
    );
}
