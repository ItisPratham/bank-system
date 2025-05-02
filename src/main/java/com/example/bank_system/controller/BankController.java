package com.example.bank_system.controller;

import com.example.bank_system.dto.request.RegisterUserRequest;
import com.example.bank_system.dto.request.TransactionRequest;
import com.example.bank_system.exception.InsufficientBalanceException;
import com.example.bank_system.exception.UserNotFoundException;
import com.example.bank_system.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bank")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @PostMapping("/registerUser")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequest registerUserRequest){
        int accId = bankService.registerUser(registerUserRequest);
        return ResponseEntity.ok(String.format("User created with Id: %d", accId));
    }

    @GetMapping("/getBalance/{userId}")
    public ResponseEntity<String> getBalance(@PathVariable("userId") int userId){
        try {
            return ResponseEntity.ok(bankService.getBalance(userId));
        }
        catch (UserNotFoundException e){
            return ResponseEntity.status(404).body(null);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/transact")
    public ResponseEntity<String> transact(@RequestBody TransactionRequest transactionRequest){
        try {
            return ResponseEntity.ok(bankService.transfer(transactionRequest));
        }
        catch (UserNotFoundException e){
            return ResponseEntity.status(404).body(null);
        }
        catch (InsufficientBalanceException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(null);
        }
    }
}
