package com.example.bank_system.service;

import com.example.bank_system.dto.request.RegisterUserRequest;
import com.example.bank_system.dto.request.TransactionRequest;
import com.example.bank_system.exception.InsufficientBalanceException;
import com.example.bank_system.exception.UserNotFoundException;
import com.example.bank_system.model.NotificationChannel;
import com.example.bank_system.model.User;
import com.example.bank_system.repository.IUserRepository;
import com.example.bank_system.service.factory.NotificationServiceFactory;
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
//    private INotificationService iNotificationService;


    public int registerUser(RegisterUserRequest registerUserRequest) {
        final User user = new User(registerUserRequest.getName(), registerUserRequest.getBalance(), registerUserRequest.getNotificationChannel());
        User createdUser = userRepository.createUser(user);
        return createdUser.getUserId();
    }

    public String getBalance(int userId) throws UserNotFoundException {
//        INotificationService iNotificationService;
        final User user = userRepository.getAUser(userId);
        INotificationService iNotificationService = notificationServiceFactory.getNotificationService(user.getNotificationChannel());
        iNotificationService.sendNotification(); // it would handle either sms
        //System.out.println("LOG: " + LocalTime.now() + " Id: " + userId + " CheckedBalance");
        return String.format("Mr %s,\nYour current balance is %.2f.\nSent via %s.", user.getName(), user.getBalance(), user.getNotificationChannel());
    }

    public User getAUser(int userId) throws UserNotFoundException {
        return userRepository.getAUser(userId);
    }

    public String transfer(TransactionRequest transactionRequest) throws UserNotFoundException, InsufficientBalanceException {
        final User fromUser = userRepository.getAUser(transactionRequest.getFromAccId());
        if (fromUser.getBalance() >= transactionRequest.getAmount()) {
            final User toUser = userRepository.getAUser(transactionRequest.getToAccId());
            fromUser.setBalance(fromUser.getBalance() - transactionRequest.getAmount());
            toUser.setBalance(toUser.getBalance() + transactionRequest.getAmount());
            //System.out.printf("LOG:%s.TransactionSuccessful.Amount %.2f.From %d.To %d%n", LocalTime.now(), transactionRequest.getAmount(), fromUser.getUserId(), toUser.getUserId());
            log.info(String.format("Transaction from %d acc to %d, value : %s", transactionRequest.getFromAccId(), transactionRequest.getToAccId(), transactionRequest.getAmount()));
            return String.format("Transaction successful. Amount of %.2f, transferred from %d to %d", transactionRequest.getAmount(), fromUser.getUserId(), toUser.getUserId());
        } else {
            throw new InsufficientBalanceException(String.format("User id %d, does not have sufficient balance", fromUser.getUserId()));
        }

    }
}
