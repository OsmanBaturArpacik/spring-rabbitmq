package com.microservice.ms2.service;

import com.microservice.ms2.api.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class MessageListenerWithExpiry {
//    private static final long EXPIRY_TIME = TimeUnit.MINUTES.toMillis(10);
    private static final long EXPIRY_TIME = TimeUnit.SECONDS.toMillis(5);
    private static Map<String, Long> messageTimestampMap = new ConcurrentHashMap<>();

    private RabbitTemplate rabbitTemplate;

    private EmailService emailService;

    public MessageListenerWithExpiry(RabbitTemplate rabbitTemplate, EmailService emailService) {
        this.rabbitTemplate = rabbitTemplate;
        this.emailService = emailService;
        this.startExpiryThread();
    }

    private void startExpiryThread() {
        new Thread(() -> {
            while (true) {
                for (Map.Entry<String, Long> entry : messageTimestampMap.entrySet()) {
                    if (System.currentTimeMillis() - entry.getValue() > EXPIRY_TIME) {
                        System.out.println("Message expired. Adding to ExpiredQueue");
                        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_KEY + "." + RabbitMqConfig.EXPIRED_QUEUE, "Expired message: " + entry.getKey());
                        messageTimestampMap.remove(entry.getKey());
                    }
                }
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }


    @RabbitListener(queues = RabbitMqConfig.MY_QUEUE)
    public void receiveMessage(String message) {
        System.out.println("Received message in ms1 from MyQueue. Message:" + message);
        messageTimestampMap.put(message, System.currentTimeMillis());

        String recipient = "recipient@example.com";
        String subject = "New Message from RabbitMQ";
        String text = message;

        emailService.sendEmail(recipient, subject, text);
    }



}
