package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.Card.Card;
import org.poo.User.User;

public final class DeleteCardCommand implements Command {
    private final Account account;
    private final Card card;
    private final int timestamp;
    private final User user;

    public DeleteCardCommand(final User user, final Account account,
                             final Card card, final int timestamp) {
        this.account = account;
        this.card = card;
        this.timestamp = timestamp;
        this.user = user;
    }

    @Override
    public void execute(final ArrayNode output) {
        CommandUtils.deleteCard(user, account, card, timestamp);
    }
}
