package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.User.User;

public final class SetAliasCommand implements Command {
    private String alias;
    private User user;
    private Account account;
    private int timestamp;

    public SetAliasCommand(final User user, final Account account,
                           final String alias, final int timestamp) {
        this.alias = alias;
        this.timestamp = timestamp;
        this.account = account;
        this.user = user;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(final Account account) {
        this.account = account;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    @Override
    public void execute(final ArrayNode output) {
        CommandUtils.setAlias(user, account, alias);
    }
}
