package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;

public class SetMinimumBalanceCommand implements Command{
    private double amount;
    private Account account;
    private int timestamp;

    public SetMinimumBalanceCommand(Account account, double amount, int timestamp) {
        this.amount = amount;
        this.account = account;
        this.timestamp = timestamp;
    }

    @Override
    public void execute(ArrayNode output) {
        CommandUtils.setMinimumBalance(account, amount, timestamp, output);
    }
}
