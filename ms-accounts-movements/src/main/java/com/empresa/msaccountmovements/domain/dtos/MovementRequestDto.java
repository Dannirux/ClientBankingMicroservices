package com.empresa.msaccountmovements.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MovementRequestDto {
    @NotNull(message = "El tipo de movimiento es obligatorio")
    private String tipoMovimiento;

    @NotBlank(message = "El número de cuenta no puede estar vacío")
    private String cuenta;

    @NotNull(message = "El valor del movimiento no puede estar vacío")
    private Double valor;
}
