package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.User.User;

public class PrintTransactionsCommand implements Command{
    private int timestamp;
    private User user;

    public PrintTransactionsCommand(int timestamp, User user) {
        this.timestamp = timestamp;
        this.user = user;
    }


    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void execute(ArrayNode output) {
        CommandUtils.printTransactions(user, timestamp, output);
    }
}
