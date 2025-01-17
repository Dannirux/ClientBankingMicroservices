package com.empresa.msaccountmovements.domain.models;

import com.empresa.msaccountmovements.domain.models.enums.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cuentas")
public class Account {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private String id;

    @NotBlank(message = "El número de cuenta no puede estar vacío")
    @Size(max = 20, message = "El número de cuenta no puede tener más de 20 caracteres")
    @Column(name = "numero_cuenta", nullable = false, unique = true, length = 20)
    private String numeroCuenta;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false)
    private AccountType tipoCuenta;

    @NotNull(message = "El saldo inicial no puede estar vacío")
    @Min(value = 0, message = "El saldo inicial no puede ser negativo")
    @Column(name = "saldo_inicial", nullable = false)
    private Double saldoInicial;

    @NotNull(message = "El estado de la cuenta es obligatorio")
    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @NotBlank(message = "El ID del cliente no puede estar vacío")
    @Column(name = "cliente_id", nullable = false)
    private String clienteId;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Movement> movements = new ArrayList<>();

    @PrePersist
    private void generateId() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
