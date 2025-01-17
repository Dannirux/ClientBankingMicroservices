package com.empresa.mspersonclient.domain.events;

import com.empresa.mspersonclient.infrastructure.messaging.RabbitMQConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ClientUpdatedEvent extends CommonEvent {

    private final String clientId;
    private final String updatedField;

    public ClientUpdatedEvent(String clientId, String updatedField) {
        super(RabbitMQConfig.CLIENT_UPDATED_QUEUE);
        this.clientId = clientId;
        this.updatedField = updatedField;
    }
}