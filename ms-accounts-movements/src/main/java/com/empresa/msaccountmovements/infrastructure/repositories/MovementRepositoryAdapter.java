package com.empresa.msaccountmovements.infrastructure.repositories;

import com.empresa.msaccountmovements.domain.models.Movement;
import com.empresa.msaccountmovements.domain.ports.MovementRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class MovementRepositoryAdapter implements MovementRepositoryPort {

    private final JpaMovementRepository jpaRepository;

    public MovementRepositoryAdapter(JpaMovementRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Movement save(Movement movement) {
        return jpaRepository.save(movement);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Movement> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Movement> findByAccountId(String accountId) {
        return jpaRepository.findByCuenta_Id(accountId);
    }

    @Override
    public List<Movement> findByClientIdAndDateRange(String clientId, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByClientIdAndDateRange(clientId, startDate, endDate);
    }

    @Override
    public List<Movement> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByDateRange(startDate, endDate);
    }

    @Override
    public List<Movement> findByCuenta_NumeroCuenta(String accountId) {
        return jpaRepository.findByCuenta_NumeroCuenta(accountId);
    }

    @Override
    public List<Movement> findByCuenta_IdAndFechaBetween(String cuentaId, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByCuenta_IdAndFechaBetween(cuentaId, startDate, endDate);
    }

    @Override
    public Optional<Double> findSaldoBeforeDate(String id, LocalDateTime startDate) {
        return jpaRepository.findSaldoBeforeDate(id, startDate);
    }
}
