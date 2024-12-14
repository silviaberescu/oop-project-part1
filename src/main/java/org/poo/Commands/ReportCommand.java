package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.User.User;

public class ReportCommand implements Command {
    private Account account;
    private User user;
    private int startTimestamp;
    private int endTimestamp;
    private int timestamp;

    public ReportCommand(User user, Account account, int startTimestamp, int endTimestamp, int timestamp) {
        this.account = account;
        this.timestamp = timestamp;
        this.endTimestamp = endTimestamp;
        this.startTimestamp = startTimestamp;
        this.user = user;
    }

    @Override
    public void execute(ArrayNode output) {
        CommandUtils.report(user, account, startTimestamp, endTimestamp, timestamp, output);
    }
}
