package com.wallet.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(BigDecimal balance) {
        super("Insufficient funds. Available balance: " + balance);
    }
}