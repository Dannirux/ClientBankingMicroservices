package com.empresa.msaccountmovements.infrastructure.messaging;

import com.empresa.msaccountmovements.domain.dtos.ClientDto;
import com.empresa.msaccountmovements.domain.events.ClientCreatedEvent;
import com.empresa.msaccountmovements.domain.events.ClientDeletedEvent;
import com.empresa.msaccountmovements.domain.events.ClientRetrievedEvent;
import com.empresa.msaccountmovements.infrastructure.adapters.ClientAsyncAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableRabbit
public class RabbitMQClientEventListener {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQClientEventListener.class);

    private final ObjectMapper objectMapper;
    private final ClientAsyncAdapter clientAsyncAdapter;

    @RabbitListener(queues = RabbitMQConfig.CLIENT_REPLY_QUEUE)
    public void handleClientRetrievedEvent(ClientRetrievedEvent event) {
        try {
            logger.info("Evento recibido en CLIENT_REPLY_QUEUE con ID de correlación: {}", event.getCorrelationId());
            ClientDto clientDto = objectMapper.readValue(event.getJsonClient(), ClientDto.class);
            logger.info("Cliente recibido con ID: {}", clientDto.getId());
            clientAsyncAdapter.updateClientCache(clientDto);
        } catch (Exception e) {
            logger.error("Error al procesar el evento ClientRetrievedEvent: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.CLIENT_CREATED_QUEUE)
    public void handleClientCreatedEvent(ClientCreatedEvent event) {
        try {
            logger.info("Evento recibido en CLIENT_CREATED_QUEUE");
        } catch (Exception e) {
            logger.error("Error al procesar el evento en CLIENT_CREATED_QUEUE: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.CLIENT_DELETED_QUEUE)
    public void handleClientDeletedEvent(ClientDeletedEvent event) {
        try {
            logger.info("Evento recibido en CLIENT_DELETED_QUEUE");
        } catch (Exception e) {
            logger.error("Error al procesar el evento en CLIENT_DELETED_QUEUE: {}", e.getMessage(), e);
        }
    }
}
