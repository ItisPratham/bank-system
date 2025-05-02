package com.example.bank_system.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService implements INotificationService{



    @Override
    public void sendNotification() {
        log.info("EMAIL sent");
    }
}
