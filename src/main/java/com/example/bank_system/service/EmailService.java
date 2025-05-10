package com.example.bank_system.service;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService implements INotificationService{

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendNotification(String msg, String to, String subject) throws MessagingException {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(msg);
            mailSender.send(message);
            log.info("EMAIL sent");
        } catch (MailException e) {
            log.error("Email sending failed", e);
            throw new MessagingException("Email sending failed", e);
        }
    }
}
