package com.microservice.ms1.service;

import com.microservice.ms1.api.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ExpiredThreadListener {
    @RabbitListener(queues = RabbitMqConfig.EXPIRED_QUEUE)
    public void receiveMessage(String message) {
        System.out.println("Received message in ms1 from ExpiredQueue. Message:" + message);
    }

}
