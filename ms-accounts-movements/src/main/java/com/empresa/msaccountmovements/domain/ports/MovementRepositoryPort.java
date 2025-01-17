package com.empresa.msaccountmovements.domain.ports;

import com.empresa.msaccountmovements.domain.models.Movement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovementRepositoryPort {
    Movement save(Movement movement);
    void deleteById(String id);
    List<Movement> findAll();
    List<Movement> findByAccountId(String accountId);
    List<Movement> findByClientIdAndDateRange(String clientId, LocalDateTime startDate, LocalDateTime endDate);
    List<Movement> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Movement> findByCuenta_NumeroCuenta(String cuentaId);
    List<Movement> findByCuenta_IdAndFechaBetween(String cuentaId, LocalDateTime startDate, LocalDateTime endDate);
    Optional<Double> findSaldoBeforeDate (String id, LocalDateTime startDate);

}
