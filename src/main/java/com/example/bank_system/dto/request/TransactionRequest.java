package com.example.bank_system.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransactionRequest {
    private int fromAccId;
    private int toAccId;
    private double amount;
}
