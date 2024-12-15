package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.Bank.ExchangeRate;
import org.poo.User.User;

import java.util.ArrayList;

public final class SendMoneyCommand implements Command {
    private final User senderUser;
    private final User receiverUser;
    private final Account sender;
    private final Account receiver;
    private final double amount;
    private final int timestamp;
    private final String description;
    private final ArrayList<ExchangeRate> exchanges;

    public SendMoneyCommand(final User senderUser, final User receiverUser,
                            final Account sender, final Account receiver,
                            final double amount, final String description,
                            final ArrayList<ExchangeRate> exchanges,
                            final int timestamp) {
        this.exchanges = exchanges;
        this.description = description;
        this.timestamp = timestamp;
        this.amount = amount;
        this.receiver = receiver;
        this.sender = sender;
        this.senderUser = senderUser;
        this.receiverUser = receiverUser;
    }

    @Override
    public void execute(final ArrayNode output) {
        CommandUtils.sendMoney(senderUser, receiverUser, sender,
                receiver, amount, description, exchanges, timestamp);
    }
}
