package org.poo.Bank;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.User.User;
import org.poo.fileio.ObjectInput;

import java.util.ArrayList;

import static org.poo.Bank.BankUtils.manageExchangeRates;
import static org.poo.Bank.BankUtils.manageUsers;

public final class Bank {
    private final ArrayList<User> users;
    private final ArrayList<ExchangeRate> exchanges;
    private static Bank bank;

    private Bank(final ObjectInput inputData) {
        this.exchanges = manageExchangeRates(inputData.getExchangeRates());
        this.users = manageUsers(inputData.getUsers());
    }

    public static Bank getBankInstance(final ObjectInput inputData) {
        if (bank == null) {
            bank = new Bank(inputData);
        }
        return bank;
    }

    // Method to clear the singleton instance
    public static void clearBankInstance() {
        bank = null;
    }
    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<ExchangeRate> getExchanges() {
        return exchanges;
    }

}
