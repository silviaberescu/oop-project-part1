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

    public Bank(final ObjectInput inputData, final ArrayNode output) {
        this.exchanges = manageExchangeRates(inputData.getExchangeRates());
        this.users = manageUsers(inputData.getUsers());
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<ExchangeRate> getExchanges() {
        return exchanges;
    }

}
