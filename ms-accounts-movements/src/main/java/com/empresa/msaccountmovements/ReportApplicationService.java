package com.empresa.msaccountmovements;

import com.empresa.msaccountmovements.domain.dtos.AccountReportDto;
import com.empresa.msaccountmovements.domain.dtos.MovementRequestDto;
import com.empresa.msaccountmovements.domain.dtos.ReportResponseDto;
import com.empresa.msaccountmovements.domain.models.Account;
import com.empresa.msaccountmovements.domain.models.Movement;
import com.empresa.msaccountmovements.domain.ports.AccountRepositoryPort;
import com.empresa.msaccountmovements.domain.ports.MovementRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportApplicationService {

    private final AccountRepositoryPort accountRepository;
    private final MovementRepositoryPort movementRepository;


    public ReportResponseDto generateAccountStatement(String clienteId, LocalDate startDate, LocalDate endDate) {
        List<Account> accounts = accountRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("El cliente con ID " + clienteId + " no existe."));

        List<AccountReportDto> accountReports = new ArrayList<>();

        for (Account account : accounts) {
            List<Movement> movementsInRange = movementRepository.findByCuenta_IdAndFechaBetween(
                    account.getId(),
                    startDate.atStartOfDay(),
                    endDate.atTime(23, 59)
            );

            Double saldoInicial = movementRepository.findSaldoBeforeDate(account.getId(), startDate.atStartOfDay())
                    .orElse(account.getSaldoInicial());

            Double saldoFinal = saldoInicial;
            for (Movement movement : movementsInRange) {
                saldoFinal = movement.getSaldo();
            }

            List<MovementRequestDto> movementDtos = movementsInRange.stream()
                    .map(movement -> new MovementRequestDto(
                            movement.getFecha().toString(),
                            movement.getTipoMovimiento().name(),
                            movement.getValor()
                    ))
                    .collect(Collectors.toList());

            accountReports.add(new AccountReportDto(
                    account.getNumeroCuenta(),
                    account.getTipoCuenta(),
                    saldoInicial,
                    saldoFinal,
                    movementDtos
            ));
        }

        return new ReportResponseDto(clienteId, accountReports);
    }
}
