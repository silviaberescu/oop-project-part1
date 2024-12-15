package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.User.User;

import java.util.ArrayList;

public final class PrintUsersCommand implements Command {
    private final ArrayList<User> users;
    private final int timestamp;

    public PrintUsersCommand(final ArrayList<User> users, final int timestamp) {
        this.users = users;
        this.timestamp = timestamp;
    }

    @Override
    public void execute(final ArrayNode output) {
        CommandUtils.printUsers(users, timestamp, output);
    }
}
