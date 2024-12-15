package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.User.User;

public final class ReportCommand implements Command {
    private final Account account;
    private final User user;
    private final int startTimestamp;
    private final int endTimestamp;
    private final int timestamp;

    public ReportCommand(final User user, final Account account,
                         final int startTimestamp, final int endTimestamp,
                         final int timestamp) {
        this.account = account;
        this.timestamp = timestamp;
        this.endTimestamp = endTimestamp;
        this.startTimestamp = startTimestamp;
        this.user = user;
    }

    @Override
    public void execute(final ArrayNode output) {
        CommandUtils.report(user, account, startTimestamp, endTimestamp, timestamp, output);
    }
}
