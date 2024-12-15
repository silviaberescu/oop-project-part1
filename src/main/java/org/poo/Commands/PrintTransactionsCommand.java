package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.User.User;

public final class PrintTransactionsCommand implements Command {
    private int timestamp;
    private User user;

    public PrintTransactionsCommand(final int timestamp, final User user) {
        this.timestamp = timestamp;
        this.user = user;
    }


    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    @Override
    public void execute(final ArrayNode output) {
        CommandUtils.printTransactions(user, timestamp, output);
    }
}
