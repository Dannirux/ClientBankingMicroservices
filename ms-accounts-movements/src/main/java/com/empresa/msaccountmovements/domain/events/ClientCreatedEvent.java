package com.empresa.msaccountmovements.domain.events;

import com.empresa.msaccountmovements.infrastructure.messaging.RabbitMQConfig;
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