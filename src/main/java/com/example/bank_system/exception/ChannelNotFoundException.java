package com.example.bank_system.exception;

public class ChannelNotFoundException extends Exception{
    public ChannelNotFoundException(final String e){
        super(e);
    }

    public ChannelNotFoundException(final String e, final Throwable cause){
        super(e, cause);
    }
}
