package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;

public final class AddFundsCommand implements Command {
    private final double amount;
    private final Account account;

    public AddFundsCommand(final Account account,
                           final double amount) {
        this.amount = amount;
        this.account = account;
    }

    @Override
    public void execute(final ArrayNode output) {
        CommandUtils.addFunds(account, amount);
    }
}
