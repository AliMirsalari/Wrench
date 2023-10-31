package com.ali.mirsalari.wrench.exception;

public class DeactivatedAccountException extends RuntimeException{
    public DeactivatedAccountException(String message) {
        super(message);
    }
}
