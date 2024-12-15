package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.User.User;

public final class DeleteAccountCommand implements Command {
    private final User user;
    private final Account account;
    private final int timestamp;

    public DeleteAccountCommand(final User user, final Account account,
                                final int timestamp) {
        this.user = user;
        this.account = account;
        this.timestamp = timestamp;
    }

    @Override
    public void execute(final ArrayNode output) {
        CommandUtils.deleteAccount(account, user, timestamp, output);
    }
}
