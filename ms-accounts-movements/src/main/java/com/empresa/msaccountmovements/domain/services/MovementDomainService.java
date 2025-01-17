package com.empresa.msaccountmovements.domain.services;

import com.empresa.msaccountmovements.application.exceptions.InsufficientBalanceException;
import com.empresa.msaccountmovements.application.mappers.MovementMapperApplication;
import com.empresa.msaccountmovements.domain.dtos.ClientDto;
import com.empresa.msaccountmovements.domain.dtos.MovementListDto;
import com.empresa.msaccountmovements.domain.dtos.MovementRequestDto;
import com.empresa.msaccountmovements.domain.models.Account;
import com.empresa.msaccountmovements.domain.models.Movement;
import com.empresa.msaccountmovements.domain.ports.AccountRepositoryPort;
import com.empresa.msaccountmovements.domain.ports.ClientServicePort;
import com.empresa.msaccountmovements.domain.ports.MovementRepositoryPort;
import com.empresa.msaccountmovements.application.exceptions.ClientNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovementDomainService {

    private final MovementRepositoryPort movementRepository;
    private final AccountRepositoryPort accountRepository;
    private final ClientServicePort clientServicePort;
    private final MovementMapperApplication movementMapper;

    public MovementDomainService(MovementRepositoryPort movementRepository,
                                 AccountRepositoryPort accountRepository,
                                 @Qualifier("rabbit") ClientServicePort clientServicePort, MovementMapperApplication movementMapper) {
        this.movementRepository = movementRepository;
        this.accountRepository = accountRepository;
        this.clientServicePort = clientServicePort;
        this.movementMapper = movementMapper;
    }

    public Movement registerMovement(MovementRequestDto dto) {
        Account account = accountRepository.findByNumeroCuenta(dto.getCuenta())
                .orElseThrow(() -> new IllegalArgumentException("La cuenta no existe"));

        if (dto.getTipoMovimiento().equalsIgnoreCase("RETIRO") && account.getSaldoInicial() < dto.getValor()) {
            throw new InsufficientBalanceException("Saldo no disponible");
        }

        double nuevoSaldo = dto.getTipoMovimiento().equalsIgnoreCase("RETIRO")
                ? account.getSaldoInicial() - dto.getValor()
                : account.getSaldoInicial() + dto.getValor();

        account.setSaldoInicial(nuevoSaldo);
        accountRepository.save(account);

        Movement movement = movementMapper.toEntity(dto);
        movement.setSaldo(nuevoSaldo);
        movement.setCuenta(account);

        return movementRepository.save(movement);
    }

    public void deleteMovement(String movementId) {
        movementRepository.deleteById(movementId);
    }

    public List<Movement> getMovementsByAccount(String accountId) {
        Account account = accountRepository.findByNumeroCuenta(accountId)
                .orElseThrow(() -> new IllegalArgumentException("La cuenta no existe"));
        return movementRepository.findByCuenta_NumeroCuenta(accountId);
    }

    public List<MovementListDto> listMovementsByClientAndDateRange(String clientId, LocalDate startDate, LocalDate endDate) {
        List<Account> accounts = accountRepository.findByClienteId(clientId).orElse(new ArrayList<>());

        if (accounts.isEmpty()) {
            throw new IllegalArgumentException("The client has no associated accounts.");
        }

        ClientDto client = clientServicePort.getClientById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        return accounts.stream()
                .flatMap(account -> {
                    List<Movement> movements = movementRepository.findByAccountId(account.getId()).stream()
                            .filter(movement -> !movement.getFecha().isBefore(startDate.atStartOfDay()) &&
                                    !movement.getFecha().isAfter(endDate.atTime(23, 59)))
                            .collect(Collectors.toList());

                    return movements.stream().map(movement -> new MovementListDto(
                            movement.getId(),
                            movement.getFecha().toString(),
                            movement.getTipoMovimiento().name(),
                            movement.getValor(),
                            movement.getSaldo(),
                            movement.getCuenta().getNumeroCuenta(),
                            movement.getCuenta().getTipoCuenta().name(),
                            client.getNombre()
                    ));
                })
                .collect(Collectors.toList());
    }
    public List<MovementListDto> listMovementsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Movement> movements = movementRepository.findByDateRange(startDate.atStartOfDay(), endDate.atTime(23, 59));

        return movements.stream().map(movement -> {
            var client = clientServicePort.getClientById(movement.getCuenta().getClienteId())
                    .orElseThrow(() -> new ClientNotFoundException("Client not found"));

            return new MovementListDto(
                    movement.getId(),
                    movement.getFecha().toString(),
                    movement.getTipoMovimiento().name(),
                    movement.getValor(),
                    movement.getSaldo(),
                    movement.getCuenta().getNumeroCuenta(),
                    movement.getCuenta().getTipoCuenta().name(),
                    client.getNombre()
            );
        }).collect(Collectors.toList());
    }

}
