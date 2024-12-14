package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.User.User;

public class ChangeInterestCommand implements Command {
    private Account account;
    private User user;
    private int timestamp;
    private double interstRate;

    public ChangeInterestCommand(User user, Account account, double interestRate, int timestamp) {
        this.account = account;
        this.timestamp = timestamp;
        this.user = user;
        this.interstRate = interestRate;
    }

    @Override
    public void execute(ArrayNode output) {
        CommandUtils.changeInterest(user, account, interstRate, timestamp, output);
    }
}
