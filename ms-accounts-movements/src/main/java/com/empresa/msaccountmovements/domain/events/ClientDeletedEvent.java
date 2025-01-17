package com.empresa.msaccountmovements.domain.events;

import com.empresa.msaccountmovements.infrastructure.messaging.RabbitMQConfig;
import lombok.Getter;

@Getter
public class ClientDeletedEvent extends CommonEvent {

    private final String clientId;

    public ClientDeletedEvent(String clientId) {
        super(RabbitMQConfig.CLIENT_DELETED_QUEUE);
        this.clientId = clientId;
    }
}