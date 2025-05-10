package com.example.bank_system.controller;

import com.example.bank_system.dto.request.RegisterUserRequest;
import com.example.bank_system.dto.request.TransactionRequest;
import com.example.bank_system.exception.InsufficientBalanceException;
import com.example.bank_system.exception.UserNotFoundException;
import com.example.bank_system.service.BankService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bank")
@RequiredArgsConstructor
@Slf4j
public class BankController {

    private final BankService bankService;

    @PostMapping("/registerUser")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequest registerUserRequest){
        try {
            int accId = 0;
            accId = bankService.registerUser(registerUserRequest);
            return ResponseEntity.ok(String.format("User created with Id: %d", accId));
        } catch (MessagingException e) {
            log.error("Message sent failure ", e);
            return ResponseEntity.status(502).body("Message failure");
        }
    }

    @GetMapping("/getBalance/{userId}")
    public ResponseEntity<String> getBalance(@PathVariable("userId") int userId){
        try {
            return ResponseEntity.ok(String.format("Balance : %s", bankService.getBalance(userId)));
        }
        catch (UserNotFoundException e){
            log.error("User not found ", e);
            return ResponseEntity.status(404).body(null);
        }
        catch (MessagingException e){
            log.error("Message sent failure ", e);
            return ResponseEntity.status(502).body("Message failure");
        }
        catch (Exception e){
            log.error("Error: ", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/transact")
    public ResponseEntity<String> transact(@RequestBody TransactionRequest transactionRequest){
        try {
            return ResponseEntity.ok(String.format("Transaction completed:%s", bankService.transfer(transactionRequest)));
        }
        catch (UserNotFoundException e){
            log.error("User not found ", e);
            return ResponseEntity.status(404).body(null);
        }
        catch (InsufficientBalanceException e){
            log.error("Insufficient balance ", e);
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (MessagingException e){
            log.error("Message sent failure ", e);
            return ResponseEntity.status(502).body("Message failure");
        }
        catch (Exception e){
            log.error("Error: ", e);
            return ResponseEntity.status(500).body(null);
        }
    }
}
// improve email service. Email should be sent on transaction, transfer as well as creation.
// take email input from user during creation.
// same can be done with sms, although not now.