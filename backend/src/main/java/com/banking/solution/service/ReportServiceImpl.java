package com.banking.solution.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.banking.solution.domain.Client;
import com.banking.solution.domain.Movement;
import com.banking.solution.dto.AccountStatementReportDTO;
import com.banking.solution.dto.MovementDTO;
import com.banking.solution.dto.ReportAccountDTO;
import com.banking.solution.exception.NotFoundException;
import com.banking.solution.repository.ClientRepository;
import com.banking.solution.repository.MovementRepository;
import com.banking.solution.utils.MovementMapper;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final MovementRepository movementRepository;
    private final ClientRepository clientRepository;

    public ReportServiceImpl(
            MovementRepository movementRepository,
            ClientRepository clientRepository
    ) {
        this.movementRepository = movementRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public AccountStatementReportDTO accountStatement(
            Long clientId,
            LocalDateTime from,
            LocalDateTime to
    ) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        List<Movement> movements = movementRepository.report(clientId, from, to);

        Map<Long, List<Movement>> movementsByAccount
                = movements.stream().collect(Collectors.groupingBy(m -> m.getAccount().getId()));

        List<ReportAccountDTO> accounts = movementsByAccount.values().stream().map(list -> {

            Movement first = list.get(0);
            String accountNumber = first.getAccount().getAccountNumber();
            String accountType = first.getAccount().getAccountType();

            BigDecimal initialBalance = first.getBalance().subtract(first.getValue());

            BigDecimal credits = list.stream()
                    .filter(m -> m.getValue().compareTo(BigDecimal.ZERO) > 0)
                    .map(Movement::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal debits = list.stream()
                    .filter(m -> m.getValue().compareTo(BigDecimal.ZERO) < 0)
                    .map(Movement::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .abs();

            BigDecimal finalBalance = list.get(list.size() - 1).getBalance();

            List<MovementDTO> movementDtos = list.stream()
                    .map(MovementMapper::toDTO)
                    .toList();

            return new ReportAccountDTO(
                    accountNumber,
                    accountType,
                    initialBalance,
                    credits,
                    debits,
                    finalBalance,
                    movementDtos
            );
        }).toList();

        return new AccountStatementReportDTO(
                client.getId(),
                client.getName(),
                client.getIdentification(),
                accounts
        );
    }
}
