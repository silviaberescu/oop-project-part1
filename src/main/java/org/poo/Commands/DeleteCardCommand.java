package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.Card.Card;
import org.poo.User.User;

public class DeleteCardCommand implements Command{
    private Account account;
    private Card card;
    private int timestamp;
    private User user;

    public DeleteCardCommand(User user, Account account, Card card, int timestamp) {
        this.account = account;
        this.card = card;
        this.timestamp = timestamp;
        this.user = user;
    }

    @Override
    public void execute(ArrayNode output) {
        CommandUtils.deleteCard(user, account, card, timestamp, output);
    }
}
