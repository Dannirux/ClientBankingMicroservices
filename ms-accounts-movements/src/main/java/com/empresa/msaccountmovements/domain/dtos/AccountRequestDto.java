package com.empresa.msaccountmovements.domain.dtos;

import com.empresa.msaccountmovements.domain.models.enums.AccountType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequestDto {

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private AccountType tipoCuenta;

    @NotNull(message = "El saldo inicial no puede ser nulo")
    @Min(value = 0, message = "El saldo inicial no puede ser negativo")
    private Double saldoInicial;

    @NotNull(message = "El estado de la cuenta es obligatorio")
    private Boolean estado;

    @NotBlank(message = "El ID del cliente no puede estar vacío")
    private String clienteId;
}
