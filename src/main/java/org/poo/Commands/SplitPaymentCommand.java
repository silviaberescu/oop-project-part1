package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.Bank.ExchangeRate;
import org.poo.User.User;

import java.util.ArrayList;

public final class SplitPaymentCommand implements Command {
    private final ArrayList<User> users;
    private final ArrayList<Account> accounts;
    private final double amount;
    private final String currency;
    private final int timestamp;
    private final ArrayList<ExchangeRate> exchanges;

    public SplitPaymentCommand(final ArrayList<User> users, final ArrayList<Account> accounts,
                               final double amount, final String currency,
                               final int timestamp, final ArrayList<ExchangeRate> exchanges) {
        this.users = users;
        this.currency = currency;
        this.amount = amount;
        this.accounts = accounts;
        this.timestamp = timestamp;
        this.exchanges = exchanges;
    }

    @Override
    public void execute(final ArrayNode output) {
        CommandUtils.splitPayment(users, accounts, amount, currency, timestamp, exchanges);
    }
}
