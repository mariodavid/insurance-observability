package com.insurance.policy.view.policy;

public class BalanceCalculationFailedException extends RuntimeException{
    public BalanceCalculationFailedException(String message) {
        super(message);
    }
}
