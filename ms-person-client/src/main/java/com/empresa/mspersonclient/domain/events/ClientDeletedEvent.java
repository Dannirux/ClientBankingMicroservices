package com.empresa.mspersonclient.domain.events;

import com.empresa.mspersonclient.infrastructure.messaging.RabbitMQConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ClientDeletedEvent extends CommonEvent {

    private final String clientId;

    public ClientDeletedEvent(String clientId) {
        super(RabbitMQConfig.CLIENT_DELETED_QUEUE);
        this.clientId = clientId;
    }
}