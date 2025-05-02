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
    private NotificationChannel notificationChannel;

    @Builder
    public User(final String name, final double balance, final NotificationChannel notificationChannel) {
        this.userId = count++;
        this.name = name;
        this.balance = balance;
        this.notificationChannel = notificationChannel;
    }

}
