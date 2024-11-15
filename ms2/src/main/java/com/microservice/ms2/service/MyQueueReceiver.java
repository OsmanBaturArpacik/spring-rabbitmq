package com.microservice.ms2.service;

import com.microservice.ms2.api.config.RabbitMqConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class MyQueueReceiver {
    private final EmailService emailService;

    private final List<String> recipients = Arrays.asList(
            "huseyin.uzun@example.com",
            "ayse.topcu@example.com",
            "mehmet.kaya@example.com",
            "fatma.yildiz@example.com",
            "ali.durmaz@example.com",
            "emine.sari@example.com",
            "mustafa.kurt@example.com",
            "zeynep.celik@example.com",
            "hasan.akar@example.com",
            "sevda.guler@example.com"
    );

    private final List<String> quotes = Arrays.asList(
            "Hayat, senin düşündüğün kadar zor değil, senin zorlaştırdığın kadar zor.",
            "Bir şeyleri değiştirmek istiyorsan, önce kendini değiştir.",
            "Başarısızlık, başarının ilk adımıdır. Yeter ki vazgeçme.",
            "Her gün biraz daha iyi olmak için çabala.",
            "Zaman, sana ne verir ne alır, sen ona ne verdiğine bak.",
            "Yolun sonunda ne olduğunu bilemeyebilirsin, ama başladığın yere dönmemelisin.",
            "Herkesin bir hedefi vardır, önemli olan senin ne kadar tutkulu olduğundur.",
            "Hayat, düşündüğünden çok daha kısa. Bu yüzden her anını değerli kıl.",
            "Düşmek, kalkmaktan vazgeçmek değildir, sadece bir fırsattır.",
            "Hayallerin ne kadar büyükse, yolculuğun da o kadar zorlu olacaktır."
    );

    private final Random random = new Random();

    @RabbitListener(queues = RabbitMqConfig.MY_QUEUE)
    public void receiveMessage(String message) {
        System.out.println("Received message in ms1 from MyQueue. Message:" + message);

        String recipient = recipients.get(random.nextInt(recipients.size()));
        String subject = "New Message from ms2 by RabbitMQ";
        String text = quotes.get(random.nextInt(quotes.size()));

        emailService.sendEmail(recipient, subject, text);
    }


}
