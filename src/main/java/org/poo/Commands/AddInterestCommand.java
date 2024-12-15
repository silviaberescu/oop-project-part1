package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;


public final class AddInterestCommand implements Command {
    private final Account account;
    private final int timestamp;

    public AddInterestCommand(final Account account, final int timestamp) {
        this.account = account;
        this.timestamp = timestamp;
    }

    @Override
    public void execute(final ArrayNode output) {
        CommandUtils.addInterest(account, timestamp, output);
    }
}
