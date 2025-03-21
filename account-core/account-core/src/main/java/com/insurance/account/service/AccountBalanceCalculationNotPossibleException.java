package com.insurance.account.service;

public class AccountBalanceCalculationNotPossibleException extends RuntimeException {
    public AccountBalanceCalculationNotPossibleException(String message) {
        super(message);
    }
}
