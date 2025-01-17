package com.empresa.msaccountmovements.infrastructure.repositories;

import com.empresa.msaccountmovements.domain.models.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaMovementRepository extends JpaRepository<Movement, String> {
    List<Movement> findByCuentaId(String accountId);
    @Query("SELECT m FROM Movement m " +
            "JOIN m.cuenta a " +
            "WHERE a.clienteId = :clientId " +
            "AND m.fecha BETWEEN :startDate AND :endDate")
    List<Movement> findByClientIdAndDateRange(@Param("clientId") String clientId,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    @Query("SELECT m FROM Movement m WHERE m.fecha BETWEEN :startDate AND :endDate")
    List<Movement> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    List<Movement> findByCuenta_NumeroCuenta(String numberAccount);
    List<Movement> findByCuenta_Id(String accountId);

    List<Movement> findByCuenta_IdAndFechaBetween(String cuentaId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT m.saldo FROM Movement m WHERE m.cuenta.id = :cuentaId AND m.fecha < :fecha ORDER BY m.fecha DESC")
    Optional<Double> findSaldoBeforeDate(@Param("cuentaId") String cuentaId, @Param("fecha") LocalDateTime fecha);


}
