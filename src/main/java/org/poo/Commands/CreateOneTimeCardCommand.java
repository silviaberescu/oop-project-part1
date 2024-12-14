package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.User.User;

public class CreateOneTimeCardCommand implements Command{
    private Account account;
    private User user;
    private int timestamp;
    public CreateOneTimeCardCommand(User user, Account account, int timestamp) {
        this.user = user;
        this.account = account;
        this.timestamp = timestamp;

    }
    @Override
    public void execute(ArrayNode output) {
        CommandUtils.createOneTimeCard(user, account, timestamp);
    }
}
