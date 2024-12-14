package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.User.User;

public class AddAccountCommand implements Command {
    private User user;
    private int timestamp;
    private String currency;
    private String type;
    private double interestRate;

    public AddAccountCommand(User user, String currency, String type, double interestRate, int timestamp) {
        this.user = user;
        this.type = type;
        this.currency = currency;
        this.timestamp = timestamp;
        this.interestRate = interestRate;
    }

    @Override
    public void execute(ArrayNode output) {
        CommandUtils.addAccount(user, currency, type, interestRate, timestamp);
    }
}

