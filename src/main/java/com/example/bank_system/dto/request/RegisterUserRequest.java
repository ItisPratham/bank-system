package com.example.bank_system.dto.request;

import com.example.bank_system.model.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class RegisterUserRequest {
    private String name;
    private double balance;
    private NotificationChannel notificationChannel;
    private String email;
    private String number;
}
