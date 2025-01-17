package com.empresa.msaccountmovements.domain.dtos;

import com.empresa.msaccountmovements.domain.models.Account;
import com.empresa.msaccountmovements.domain.models.Movement;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AccountStatementReportDto {
    private String clienteId;
    private String clienteNombre;
    private List<CuentaConMovimientos> cuentas;

    @Data
    @AllArgsConstructor
    public static class CuentaConMovimientos {
        private Account cuenta;
        private List<Movement> movimientos;
    }
}
