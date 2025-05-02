package com.example.bank_system.service.factory;


import com.example.bank_system.exception.ChannelNotFoundException;
import com.example.bank_system.model.NotificationChannel;
import com.example.bank_system.service.EmailService;
import com.example.bank_system.service.INotificationService;
import com.example.bank_system.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceFactory {

    private final SmsService smsService;
    private final EmailService emailService;



    public INotificationService getNotificationService(NotificationChannel notificationChannel) {
        return switch (notificationChannel) {
            case SMS -> smsService;
            case EMAIL -> emailService;
        };
    }
}
