package com.empresa.msaccountmovements.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDto {
    private String id;
    private String nombre;
    private String identificacion;
    private Boolean estado;
}
