package com.microservice.ms2.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void sendEmail(String recipient, String subject, String text) {
        System.out.println("Email Sending To:" + recipient + "\tSubject: " + subject + "\tContent:" + text);
    }
}
