package com.empresa.msaccountmovements.infrastructure.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.AllowedListDeserializingMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String CLIENT_EXCHANGE = "client.exchange";
    public static final String CLIENT_CREATED_QUEUE = "client-created-queue";
    public static final String CLIENT_GET_QUEUE = "client-get-queue";
    public static final String CLIENT_DELETED_QUEUE = "client-deleted-queue";
    public static final String CLIENT_REPLY_QUEUE = "client-reply-queue";
    public static final String CLIENT_ROUTING_KEY_CREATED = "client.created";
    public static final String CLIENT_ROUTING_KEY_GET = "client.get";
    public static final String CLIENT_ROUTING_KEY_DELETED = "client.deleted";
    public static final String CLIENT_ROUTING_KEY_REPLY = "client.reply";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();

        // Configura paquetes confiables
        typeMapper.setTrustedPackages("com.empresa.mspersonclient.domain.events");
        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }

    @Bean
    public TopicExchange clientExchange() {
        return new TopicExchange(CLIENT_EXCHANGE);
    }

    @Bean
    public Queue clientCreatedQueue() {
        return new Queue(CLIENT_CREATED_QUEUE);
    }

    @Bean
    public Queue clientDeletedQueue() {
        return new Queue(CLIENT_DELETED_QUEUE);
    }

    @Bean
    public Queue clientGetQueue() {
        return new Queue(CLIENT_GET_QUEUE);
    }

    @Bean
    public Queue clientReplyQueue() {
        return new Queue(CLIENT_REPLY_QUEUE);
    }

    @Bean
    public Binding bindingClientCreated(Queue clientCreatedQueue, TopicExchange clientExchange) {
        return BindingBuilder.bind(clientCreatedQueue)
                .to(clientExchange)
                .with(CLIENT_ROUTING_KEY_CREATED);
    }

    @Bean
    public Binding bindingClientGet(Queue clientGetQueue, TopicExchange clientExchange) {
        return BindingBuilder.bind(clientGetQueue)
                .to(clientExchange)
                .with(CLIENT_ROUTING_KEY_GET);
    }

    @Bean
    public Binding bindingClientDeleted(Queue clientDeletedQueue, TopicExchange clientExchange) {
        return BindingBuilder.bind(clientDeletedQueue)
                .to(clientExchange)
                .with(CLIENT_ROUTING_KEY_DELETED);
    }

    @Bean
    public Binding bindingClientReply(Queue clientReplyQueue, TopicExchange clientExchange) {
        return BindingBuilder.bind(clientReplyQueue)
                .to(clientExchange)
                .with(CLIENT_ROUTING_KEY_REPLY);
    }
}
