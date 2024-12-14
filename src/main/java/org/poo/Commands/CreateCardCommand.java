package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.User.User;

public class CreateCardCommand implements Command{
    private Account account;
    private User user;
    private int timestamp;
    public CreateCardCommand(Account account, User user, int timestamp) {
        this.user = user;
        this.account = account;
        this.timestamp = timestamp;
    }

    @Override
    public void execute(ArrayNode output) {
        CommandUtils.createCard(account, user, timestamp);
    }
}
