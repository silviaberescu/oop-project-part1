package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.User.User;

public final class CreateOneTimeCardCommand implements Command {
    private final Account account;
    private final User user;
    private final int timestamp;
    public CreateOneTimeCardCommand(final User user,
                                    final Account account,
                                    final int timestamp) {
        this.user = user;
        this.account = account;
        this.timestamp = timestamp;

    }
    @Override
    public void execute(final ArrayNode output) {
        CommandUtils.createOneTimeCard(user, account, timestamp);
    }
}
