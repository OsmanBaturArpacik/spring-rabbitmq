package com.microservice.ms2.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MyQueueReceiver {

//    @RabbitListener(queues = "MyQueue")
//    public void receiveMessage(String message) {
//        System.out.println("Received message: " + message);
//    }

}
