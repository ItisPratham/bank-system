package com.example.bank_system.service;

import com.example.bank_system.dto.request.RegisterUserRequest;
import com.example.bank_system.dto.request.TransactionRequest;
import com.example.bank_system.exception.InsufficientBalanceException;
import com.example.bank_system.exception.UserNotFoundException;
import com.example.bank_system.model.NotificationChannel;
import com.example.bank_system.model.User;
import com.example.bank_system.repository.IUserRepository;
import com.example.bank_system.service.factory.NotificationServiceFactory;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankService {
    private final IUserRepository userRepository;
    private final NotificationServiceFactory notificationServiceFactory;


    public int registerUser(RegisterUserRequest registerUserRequest) {
        final User user = new User(registerUserRequest.getName(), registerUserRequest.getBalance(), registerUserRequest.getNotificationChannel());
        User createdUser = userRepository.createUser(user);
        return createdUser.getUserId();
    }

    public double getBalance(int userId) throws UserNotFoundException, MessagingException {
        final User user = userRepository.getAUser(userId);
        INotificationService iNotificationService = notificationServiceFactory.getNotificationService(user.getNotificationChannel());
        iNotificationService.sendNotification();
        log.info(String.format("User ID %d checked balance %s", user.getUserId(), user.getBalance()));
        return user.getBalance();
    }

    public User getAUser(int userId) throws UserNotFoundException {
        return userRepository.getAUser(userId);
    }

    public boolean transfer(TransactionRequest transactionRequest) throws UserNotFoundException, InsufficientBalanceException {
        final User fromUser = userRepository.getAUser(transactionRequest.getFromAccId());
        if (fromUser.getBalance() >= transactionRequest.getAmount()) {
            final User toUser = userRepository.getAUser(transactionRequest.getToAccId());
            fromUser.setBalance(fromUser.getBalance() - transactionRequest.getAmount());
            toUser.setBalance(toUser.getBalance() + transactionRequest.getAmount());
            log.info(String.format("Transaction from %d acc to %d, value : %s", transactionRequest.getFromAccId(), transactionRequest.getToAccId(), transactionRequest.getAmount()));
            return true;
        } else {
            throw new InsufficientBalanceException(String.format("User id %d, does not have sufficient balance", fromUser.getUserId()));
        }

    }
}
