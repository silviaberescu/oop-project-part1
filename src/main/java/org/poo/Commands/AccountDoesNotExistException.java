package org.poo.Commands;

public class AccountDoesNotExistException extends IllegalStateException {
    public AccountDoesNotExistException(String message) {
        super(message);
    }
}
