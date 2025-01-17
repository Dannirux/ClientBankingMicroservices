package com.empresa.mspersonclient.infrastructure.messaging;

import com.empresa.mspersonclient.domain.events.CommonEvent;
import com.empresa.mspersonclient.domain.ports.EventPublisherPort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQEventPublisherAdapter implements EventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventPublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(CommonEvent event) {
        String routingKey = event.getEventType().toLowerCase();
        rabbitTemplate.convertAndSend(RabbitMQConfig.CLIENT_EXCHANGE, routingKey, event);
        System.out.println("Evento publicado: " + event.getEventType() + " con ID: " + event.getEventId());
    }
}
