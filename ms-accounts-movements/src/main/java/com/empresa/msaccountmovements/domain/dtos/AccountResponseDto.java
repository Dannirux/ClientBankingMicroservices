package com.empresa.msaccountmovements.domain.dtos;

import com.empresa.msaccountmovements.domain.models.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponseDto {
    private String id;
    private String numeroCuenta;
    private AccountType tipoCuenta;
    private Double saldoInicial;
    private Boolean estado;
    private ClientDto cliente;

}
