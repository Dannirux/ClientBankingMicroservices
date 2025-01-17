package com.empresa.msaccountmovements.domain.dtos;

import com.empresa.msaccountmovements.domain.models.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AccountReportDto {
    private String numeroCuenta;
    private AccountType tipoCuenta;
    private Double saldoInicial;
    private Double saldoFinal;
    private List<MovementRequestDto> movimientos;
}
