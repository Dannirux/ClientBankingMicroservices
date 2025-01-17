package com.empresa.msaccountmovements.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReportResponseDto {
    private String clienteId;
    private List<AccountReportDto> cuentas;
}