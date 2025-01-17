package com.empresa.mspersonclient.infrastructure.messaging;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CLIENT_EXCHANGE = "client.exchange";
    public static final String CLIENT_CREATED_QUEUE = "client-created-queue";
    public static final String CLIENT_UPDATED_QUEUE = "client-updated-queue";
    public static final String CLIENT_GET_QUEUE = "client-get-queue";
    public static final String CLIENT_DELETED_QUEUE = "client-deleted-queue";
    public static final String CLIENT_REPLY_QUEUE = "client-reply-queue";

    public static final String CLIENT_ROUTING_KEY_CREATED = "client.created";
    public static final String CLIENT_ROUTING_KEY_UPDATED = "client.updated";
    public static final String CLIENT_ROUTING_KEY_GET = "client.get";
    public static final String CLIENT_ROUTING_KEY_DELETED = "client.deleted";
    public static final String CLIENT_ROUTING_KEY_REPLY = "client.reply";


    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();

        typeMapper.setTrustedPackages("*");

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
    public Queue clientUpdatedQueue() {
        return new Queue(CLIENT_UPDATED_QUEUE);
    }

    @Bean
    public Binding bindingClientCreated(Queue clientCreatedQueue, TopicExchange clientExchange) {
        return BindingBuilder.bind(clientCreatedQueue).to(clientExchange).with(CLIENT_ROUTING_KEY_CREATED);
    }

    @Bean
    public Binding bindingClientDeleted(Queue clientDeletedQueue, TopicExchange clientExchange) {
        return BindingBuilder.bind(clientDeletedQueue).to(clientExchange).with(CLIENT_ROUTING_KEY_DELETED);
    }

    @Bean
    public Binding bindingClientGet(Queue clientGetQueue, TopicExchange clientExchange) {
        return BindingBuilder.bind(clientGetQueue).to(clientExchange).with(CLIENT_ROUTING_KEY_GET);
    }

    @Bean
    public Binding bindingClientReply(Queue clientReplyQueue, TopicExchange clientExchange) {
        return BindingBuilder.bind(clientReplyQueue).to(clientExchange).with(CLIENT_ROUTING_KEY_REPLY);
    }

    @Bean
    public Binding bindingClientUpdated(Queue clientUpdatedQueue, TopicExchange clientExchange) {
        return BindingBuilder.bind(clientUpdatedQueue).to(clientExchange).with(CLIENT_ROUTING_KEY_UPDATED);
    }
}
