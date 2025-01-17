package com.empresa.msaccountmovements.domain.ports;

import com.empresa.msaccountmovements.domain.dtos.ClientDto;

import java.util.Optional;

public interface ClientServicePort {
    Optional<ClientDto> getClientById(String clientId);
}
