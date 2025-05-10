package com.example.bank_system.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Data
public class User {
    private static int count = 0;
    @Getter
    private final int userId;
    private final String name;
    private double balance;
    private final String email;
    private final String number;
    private NotificationChannel notificationChannel;

    @Builder
    public User(final String name, final double balance, final String email, final String number, final NotificationChannel notificationChannel) {
        this.email = email;
        this.number = number;
        this.userId = count++;
        this.name = name;
        this.balance = balance;
        this.notificationChannel = notificationChannel;
    }

    public String channel() {
        return getNotificationChannel() == NotificationChannel.EMAIL ? getEmail() : getNumber();
    }

}
