package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.User.User;

public final class ChangeInterestCommand implements Command {
    private final Account account;
    private final User user;
    private final int timestamp;
    private final double interestRate;

    public ChangeInterestCommand(final User user, final Account account,
                                 final double interestRate,
                                 final int timestamp) {
        this.account = account;
        this.timestamp = timestamp;
        this.user = user;
        this.interestRate = interestRate;
    }

    @Override
    public void execute(final ArrayNode output) {
        CommandUtils.changeInterest(user, account, interestRate, timestamp, output);
    }
}
