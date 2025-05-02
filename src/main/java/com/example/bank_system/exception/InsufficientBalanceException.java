package com.example.bank_system.exception;

public class InsufficientBalanceException extends Exception{
    public InsufficientBalanceException(final String e){
        super(e);
    }

    public InsufficientBalanceException(final String e, final Throwable cause){
        super(e, cause);
    }
}
