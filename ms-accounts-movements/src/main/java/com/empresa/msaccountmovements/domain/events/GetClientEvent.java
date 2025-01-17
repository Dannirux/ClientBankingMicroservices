package com.empresa.msaccountmovements.domain.events;

import com.empresa.msaccountmovements.infrastructure.messaging.RabbitMQConfig;
import lombok.Getter;

@Getter
public class GetClientEvent extends CommonEvent {
    private final String clientId;
    private final String updatedField;
    public GetClientEvent(String clientId, String updatedField) {
        super(RabbitMQConfig.CLIENT_ROUTING_KEY_GET);
        this.clientId = clientId;
        this.updatedField = updatedField;
    }
}
