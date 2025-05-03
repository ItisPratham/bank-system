package com.example.bank_system.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService implements INotificationService{
    @Override
    public void sendNotification(String msg) {
        log.info("SMS sent");
    }
}
