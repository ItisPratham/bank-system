package com.example.bank_system.service;

import com.example.bank_system.dto.request.RegisterUserRequest;
import com.example.bank_system.dto.request.TransactionRequest;
import com.example.bank_system.exception.InsufficientBalanceException;
import com.example.bank_system.exception.UserNotFoundException;
import com.example.bank_system.model.User;
import com.example.bank_system.repository.IUserRepository;
import com.example.bank_system.service.factory.NotificationServiceFactory;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankService {
    private final IUserRepository userRepository;
    private final NotificationServiceFactory notificationServiceFactory;


    public int registerUser(RegisterUserRequest registerUserRequest) throws MessagingException {
        final User user = new User(registerUserRequest.getName(), registerUserRequest.getBalance(), registerUserRequest.getEmail(), registerUserRequest.getNumber(), registerUserRequest.getNotificationChannel());
        User createdUser = userRepository.createUser(user);
        INotificationService iNotificationService = notificationServiceFactory.getNotificationService(user.getNotificationChannel());
        iNotificationService.sendNotification(String.format("Hello %s,\nAccount created successfully\nTime: %s", user.getName(), LocalDateTime.now()), user.getEmail(), "Successful Registration");
        return createdUser.getUserId();
    }

    public double getBalance(int userId) throws UserNotFoundException, MessagingException {
        final User user = userRepository.getAUser(userId);
        INotificationService iNotificationService = notificationServiceFactory.getNotificationService(user.getNotificationChannel());
        iNotificationService.sendNotification(String.format("Hello %s,\nYou checked your balance\nBalance: %s\nTime: %s", user.getName(), user.getBalance(), LocalDateTime.now()), user.channel(), "Account Balance Checked");
        log.info(String.format("User ID %d checked balance %s", user.getUserId(), user.getBalance()));
        return user.getBalance();
    }

    public User getAUser(int userId) throws UserNotFoundException {
        return userRepository.getAUser(userId);
    }

    public boolean transfer(TransactionRequest transactionRequest) throws UserNotFoundException, InsufficientBalanceException, MessagingException {
        final User fromUser = userRepository.getAUser(transactionRequest.getFromAccId());
        if (fromUser.getBalance() >= transactionRequest.getAmount()) {
            final User toUser = userRepository.getAUser(transactionRequest.getToAccId());
            fromUser.setBalance(fromUser.getBalance() - transactionRequest.getAmount());
            toUser.setBalance(toUser.getBalance() + transactionRequest.getAmount());
            INotificationService iNotificationService = notificationServiceFactory.getNotificationService(fromUser.getNotificationChannel());
            iNotificationService.sendNotification(String.format("Hello %s,\nYour transaction to account: %s\nDebit amount: %s\nRemaining balance: %s\nTime: %s",  fromUser.getName(), toUser.getUserId(), transactionRequest.getAmount(), fromUser.getBalance(), LocalDateTime.now()), fromUser.channel(), "Transaction Alert");
            iNotificationService = notificationServiceFactory.getNotificationService(toUser.getNotificationChannel());
            iNotificationService.sendNotification(String.format("Hello %s,\nYou have received a payment\nFrom account: %s\nCredit amount: %s\nNew balance: %s\nTime: %s", toUser.getName(), fromUser.getUserId(), transactionRequest.getAmount(), toUser.getBalance(), LocalDateTime.now()), toUser.channel(), "Transaction Alert");
            log.info(String.format("Transaction from %d acc to %d, value : %s", transactionRequest.getFromAccId(), transactionRequest.getToAccId(), transactionRequest.getAmount()));
            return true;
        } else {
            throw new InsufficientBalanceException(String.format("User id %d, does not have sufficient balance", fromUser.getUserId()));
        }

    }
}
