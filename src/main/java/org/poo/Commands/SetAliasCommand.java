package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.User.User;

public class SetAliasCommand implements Command {
    private String alias;
    private User user;
    private Account account;
    private int timestamp;

    public SetAliasCommand(User user, Account account, String alias, int timestamp) {
        this.alias = alias;
        this.timestamp = timestamp;
        this.account = account;
        this.user = user;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void execute(ArrayNode output) {
        CommandUtils.setAlias(user, account, alias, timestamp);
    }
}
