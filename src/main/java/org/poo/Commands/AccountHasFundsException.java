package org.poo.Commands;

public class AccountHasFundsException extends IllegalStateException {
    public AccountHasFundsException(String message) {
        super(message);
    }
}
