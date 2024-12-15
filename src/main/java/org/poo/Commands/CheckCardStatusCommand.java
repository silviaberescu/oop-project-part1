package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Card.Card;
import org.poo.User.User;

public final class CheckCardStatusCommand implements Command {
    private final Card card;
    private final double balance;
    private final double minBalance;
    private final int timestamp;
    private final User user;
    public CheckCardStatusCommand(final User user, final Card card,
                                  final double balance, final double minBalance,
                                  final int timestamp) {
        this.card = card;
        this.balance = balance;
        this.minBalance = minBalance;
        this.timestamp = timestamp;
        this.user = user;
    }

    @Override
    public void execute(final ArrayNode output) {
        CommandUtils.checkCardStatus(user, card, balance, minBalance,
                timestamp, output);
    }
}
