package com.insurance.account.service;

public class InvalidAccountException extends RuntimeException {
    public InvalidAccountException(String message) {
        super(message);
    }
}
