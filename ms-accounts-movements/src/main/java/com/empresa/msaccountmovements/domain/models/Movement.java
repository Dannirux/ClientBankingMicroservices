package com.empresa.msaccountmovements.domain.models;

import com.empresa.msaccountmovements.domain.dtos.MovementRequestDto;
import com.empresa.msaccountmovements.domain.models.enums.MovementType;
import com.empresa.msaccountmovements.domain.ports.AccountRepositoryPort;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movimientos")
public class Movement {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private String id;

    @ManyToOne
    @NotNull(message = "Numero de cuenta")
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Account cuenta;

    @NotNull(message = "La fecha no puede estar vacía")
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false)
    private MovementType tipoMovimiento;

    @NotNull(message = "El valor del movimiento no puede estar vacío")
    @Column(name = "valor", nullable = false)
    private Double valor;

    @NotNull(message = "El saldo resultante no puede estar vacío")
    @Column(name = "saldo", nullable = false)
    private Double saldo;

    @PrePersist
    private void generateId() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
    }
}
