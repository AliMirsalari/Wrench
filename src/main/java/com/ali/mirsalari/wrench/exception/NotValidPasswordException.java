package com.ali.mirsalari.wrench.exception;

public class NotValidPasswordException extends RuntimeException{
    public NotValidPasswordException(String message) {
        super(message);
    }
}
