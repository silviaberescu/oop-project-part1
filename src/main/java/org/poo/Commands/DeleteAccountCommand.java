package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.User.User;

public class DeleteAccountCommand implements Command{
    private User user;
    private Account account;
    private int timestamp;

    public DeleteAccountCommand(User user, Account account, int timestamp) {
        this.user = user;
        this.account = account;
        this.timestamp = timestamp;
    }

    @Override
    public void execute(ArrayNode output) {
        CommandUtils.deleteAccount(account, user, timestamp, output);
    }
}
