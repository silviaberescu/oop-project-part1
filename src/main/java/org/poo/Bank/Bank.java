package org.poo.Bank;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.Card.Card;
import org.poo.Commands.*;
import org.poo.User.User;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;

import static org.poo.Bank.BankUtils.manageExchangeRates;
import static org.poo.Bank.BankUtils.manageUsers;

public class Bank {
    private final ArrayList<User> users;
    private final ArrayList<ExchangeRate> exchanges;

    public Bank(ObjectInput inputData, ArrayNode output) {
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
