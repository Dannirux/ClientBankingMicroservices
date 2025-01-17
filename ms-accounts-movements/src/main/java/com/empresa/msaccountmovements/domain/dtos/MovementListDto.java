package com.empresa.msaccountmovements.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovementListDto {
    private String id;
    private String fecha;
    private String tipoMovimiento;
    private Double valor;
    private Double saldo;
    private String numeroCuenta;
    private String tipoCuenta;
    private String cliente;
}