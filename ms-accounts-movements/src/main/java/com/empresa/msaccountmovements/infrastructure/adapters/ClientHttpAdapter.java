package com.empresa.msaccountmovements.infrastructure.adapters;

import com.empresa.msaccountmovements.domain.dtos.ClientDto;
import com.empresa.msaccountmovements.domain.ports.ClientServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component("rest")
@RequiredArgsConstructor
public class ClientHttpAdapter implements ClientServicePort {

    private final RestTemplate restTemplate;

    @Override
    public Optional<ClientDto> getClientById(String clientId) {
        try {
            String url = "http://localhost:8080/clients/" + clientId;
            ClientDto client = restTemplate.getForObject(url, ClientDto.class);
            return Optional.ofNullable(client);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
