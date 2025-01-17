package com.empresa.msaccountmovements.application.mappers;

import com.empresa.msaccountmovements.domain.dtos.MovementRequestDto;
import com.empresa.msaccountmovements.domain.models.Account;
import com.empresa.msaccountmovements.domain.models.Movement;
import com.empresa.msaccountmovements.domain.models.enums.MovementType;
import com.empresa.msaccountmovements.domain.ports.AccountRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovementMapperApplication {

    private final AccountRepositoryPort accountRepository;

    public Movement toEntity(MovementRequestDto dto) {
        Account account = accountRepository.findByNumeroCuenta(dto.getCuenta())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        return new Movement(
                UUID.randomUUID().toString(),
                account,
                LocalDateTime.now(),
                MovementType.valueOf(dto.getTipoMovimiento()),
                dto.getValor(),
                account.getSaldoInicial()
        );
    }
}
