package com.empresa.mspersonclient.domain.events;

import com.empresa.mspersonclient.infrastructure.messaging.RabbitMQConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ClientCreatedEvent extends CommonEvent  {

    private final String clientId;
    private final String nombre;

    public ClientCreatedEvent(String clientId, String nombre) {
        super(RabbitMQConfig.CLIENT_ROUTING_KEY_CREATED);
        this.clientId = clientId;
        this.nombre = nombre;
    }
}