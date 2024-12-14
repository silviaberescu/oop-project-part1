package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;

public class AddFundsCommand implements Command{
    private double amount;
    private Account account;

    public AddFundsCommand(Account account, double amount) {
        this.amount = amount;
        this.account = account;
    }

    @Override
    public void execute(ArrayNode output) {
        CommandUtils.addFunds(account, amount);
    }
}
