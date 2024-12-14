package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.User.User;

public class AddInterestCommand implements Command {
    private Account account;
    private User user;
    private int timestamp;

    public AddInterestCommand(User user, Account account, int timestamp) {
        this.account = account;
        this.timestamp = timestamp;
        this.user = user;
    }

    @Override
    public void execute(ArrayNode output) {
        CommandUtils.addInterest(user, account, timestamp, output);
    }
}
