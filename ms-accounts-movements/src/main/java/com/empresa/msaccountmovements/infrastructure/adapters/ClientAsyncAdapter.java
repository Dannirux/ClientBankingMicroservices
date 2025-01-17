package com.empresa.msaccountmovements.infrastructure.adapters;

import com.empresa.msaccountmovements.domain.dtos.ClientDto;
import com.empresa.msaccountmovements.domain.ports.ClientServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component("rabbit")
@RequiredArgsConstructor
public class ClientAsyncAdapter implements ClientServicePort {

    private final Map<String, ClientDto> clientCache = new ConcurrentHashMap<>();

    public void updateClientCache(ClientDto clientDTO) {
        clientCache.put(clientDTO.getId(), clientDTO);
    }

    @Override
    public Optional<ClientDto> getClientById(String clientId) {
        return Optional.ofNullable(clientCache.get(clientId));
    }
}
