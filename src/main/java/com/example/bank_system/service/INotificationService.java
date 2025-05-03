package com.example.bank_system.service;

import jakarta.mail.MessagingException;

public interface INotificationService {


    void sendNotification() throws MessagingException;

}
