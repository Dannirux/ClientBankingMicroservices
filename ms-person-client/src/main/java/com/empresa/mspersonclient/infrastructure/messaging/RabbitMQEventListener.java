package com.empresa.mspersonclient.infrastructure.messaging;

import com.empresa.mspersonclient.application.ClientApplicationService;
import com.empresa.mspersonclient.domain.Client;
import com.empresa.mspersonclient.domain.events.ClientDeletedEvent;
import com.empresa.mspersonclient.domain.events.GetClientEvent;
import com.empresa.mspersonclient.domain.events.ClientRetrievedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class RabbitMQEventListener {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQEventListener.class);

    private final ClientApplicationService clientService;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public RabbitMQEventListener(ClientApplicationService clientService, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.clientService = clientService;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.CLIENT_DELETED_QUEUE)
    public void handleClientDeletedEvent(ClientDeletedEvent event) {
        logger.info("Evento recibido: Cliente eliminado con ID {}", event.getClientId());
    }

    @RabbitListener(queues = RabbitMQConfig.CLIENT_GET_QUEUE)
    public void handleClientGetEvent(GetClientEvent event) {
        logger.info("Evento recibido: Solicitud para obtener cliente con ID {}", event.getClientId());

        try {
            Client clientDto = clientService.getClientById(event.getClientId());

            String clientJson = objectMapper.writeValueAsString(clientDto);

            publishClientRetrievedEvent(event.getClientId(), clientJson);
        } catch (Exception e) {
            logger.error("Error al procesar el evento de obtener cliente con ID {}: {}", event.getClientId(), e.getMessage(), e);
        }
    }

    private void publishClientRetrievedEvent(String correlationId, String clientJson) {
        ClientRetrievedEvent clientRetrievedEvent = new ClientRetrievedEvent(correlationId, clientJson);

        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.CLIENT_EXCHANGE, RabbitMQConfig.CLIENT_ROUTING_KEY_REPLY, clientRetrievedEvent);
            logger.info("Evento ClientRetrievedEvent publicado con ID de correlación {}", correlationId);
        } catch (Exception e) {
            logger.error("Error al publicar el evento ClientRetrievedEvent con ID de correlación {}: {}", correlationId, e.getMessage(), e);
        }
    }
}
