package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Card.Card;
import org.poo.User.User;

public class CheckCardStatusCommand implements Command {
    private Card card;
    private double balance;
    private double minBalance;
    private int timestamp;
    private User user;
    public CheckCardStatusCommand(User user, Card card, double balance, double minBalance, int timestamp) {
        this.card = card;
        this.balance = balance;
        this.minBalance = minBalance;
        this.timestamp = timestamp;
        this.user = user;
    }

    @Override
    public void execute(ArrayNode output) {
        CommandUtils.checkCardStatus(user, card, balance, minBalance, timestamp, output);
    }
}
