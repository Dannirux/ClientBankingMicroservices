package com.empresa.msaccountmovements.infrastructure.messaging;

import com.empresa.msaccountmovements.domain.events.CommonEvent;
import com.empresa.msaccountmovements.domain.ports.EventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQEventPublisherAdapter implements EventPublisherPort {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQEventPublisherAdapter.class);
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventPublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(CommonEvent event) {
        try {
            String routingKey = event.getEventType().toLowerCase();
            System.out.println(routingKey);
            rabbitTemplate.convertAndSend("client.exchange", routingKey, event);
            logger.info("Evento publicado: {} con ID: {}", event.getEventType(), event.getEventId());
        } catch (Exception e) {
            logger.error("Error al publicar el evento: {}", event.getEventType(), e);
        }
    }
}
