package com.banking.solution.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountStatementReportDTO {

    private Long clientId;
    private String clientName;
    private String identification;
    private List<ReportAccountDTO> accounts;
}
