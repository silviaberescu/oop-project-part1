package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.Bank.ExchangeRate;
import org.poo.User.User;

import java.util.ArrayList;

public class SplitPaymentCommand implements Command {
    private ArrayList<User> users;
    private ArrayList<Account> accounts;
    private double amount;
    private String currency;
    private int timestamp;
    private ArrayList<ExchangeRate> exchanges;

    public SplitPaymentCommand(ArrayList<User> users, ArrayList<Account> accounts, double amount,
                               String currency, int timestamp, ArrayList<ExchangeRate> exchanges) {
        this.users = users;
        this.currency = currency;
        this.amount = amount;
        this.accounts = accounts;
        this.timestamp= timestamp;
        this.exchanges = exchanges;
    }

    @Override
    public void execute(ArrayNode output) {
        CommandUtils.splitPayment(users, accounts, amount, currency, timestamp, exchanges);
    }
}
