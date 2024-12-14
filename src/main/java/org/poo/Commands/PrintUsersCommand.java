package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.User.User;

import java.util.ArrayList;

public class PrintUsersCommand implements Command {
    private ArrayList<User> users;
    private int timestamp;

    public PrintUsersCommand(ArrayList<User> users, int timestamp) {
        this.users = users;
        this.timestamp = timestamp;
    }

    @Override
    public void execute(ArrayNode output) {
        CommandUtils.printUsers(users, timestamp, output);
    }
}
