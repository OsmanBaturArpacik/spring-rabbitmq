package com.microservice.ms1.service.api.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String MY_QUEUE = "MyQueue";
    public static final String EXPIRED_QUEUE = "ExpiredQueue";
    public static final String EXCHANGE = "MyTopicExchange";
    public static final String ROUTING_KEY = "topic";

    @Bean
    Queue myQueue() {
        return new Queue(MY_QUEUE, true);
    }
    @Bean
    Queue expiredQueue() {
        return new Queue(EXPIRED_QUEUE, true);
    }

    @Bean
    Exchange myExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    Binding myQueueBinding() {
        return BindingBuilder
                .bind(myQueue())
                .to(myExchange())
                .with(ROUTING_KEY + "." + MY_QUEUE)
                .noargs();
    }

    @Bean
    Binding expiredQueueBinding() {
        return BindingBuilder
                .bind(expiredQueue())
                .to(myExchange())
                .with(ROUTING_KEY + "." + EXPIRED_QUEUE)
                .noargs();
    }

    @Bean
    ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("rabbitmq");
        cachingConnectionFactory.setUsername("guest");
        cachingConnectionFactory.setPassword("guest");
        return cachingConnectionFactory;
    }

}

