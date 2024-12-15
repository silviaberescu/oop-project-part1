package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.User.User;

public final class AddAccountCommand implements Command {
    private final User user;
    private final int timestamp;
    private final String currency;
    private final String type;
    private final double interestRate;

    public AddAccountCommand(final User user, final String currency,
                             final String type, final double interestRate,
                             final int timestamp) {
        this.user = user;
        this.type = type;
        this.currency = currency;
        this.timestamp = timestamp;
        this.interestRate = interestRate;
    }

    @Override
    public void execute(final ArrayNode output) {
        CommandUtils.addAccount(user, currency, type, interestRate, timestamp);
    }
}

