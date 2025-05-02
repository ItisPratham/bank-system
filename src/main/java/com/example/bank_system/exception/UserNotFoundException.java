package com.example.bank_system.exception;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(final String e){
        super(e);
    }

    public UserNotFoundException(final String e, final Throwable cause){
        super(e, cause);
    }
}
