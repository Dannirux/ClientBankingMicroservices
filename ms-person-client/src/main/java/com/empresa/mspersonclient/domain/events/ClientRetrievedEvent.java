package com.empresa.mspersonclient.domain.events;

import com.empresa.mspersonclient.infrastructure.messaging.RabbitMQConfig;
import lombok.Getter;

@Getter
public class ClientRetrievedEvent extends CommonEvent {
    private final String correlationId;
    private final String jsonClient;

    public ClientRetrievedEvent(String correlationId, String client) {
        super(RabbitMQConfig.CLIENT_ROUTING_KEY_REPLY);
        this.correlationId = correlationId;
        this.jsonClient = client;
    }
}
