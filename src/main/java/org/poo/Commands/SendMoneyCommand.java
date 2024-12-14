package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.Bank.ExchangeRate;
import org.poo.User.User;

import java.util.ArrayList;

public class SendMoneyCommand implements Command {
    private User senderUser;
    private User receiverUser;
    private Account sender;
    private Account receiver;
    private double amount;
    private int timestamp;
    private String description;
    private ArrayList<ExchangeRate> exchanges;

    public SendMoneyCommand(User senderUser, User receiverUser, Account sender, Account receiver, double amount,
                            String description, ArrayList<ExchangeRate> exchanges, int timestamp) {
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
    public void execute(ArrayNode output) {
        CommandUtils.sendMoney(senderUser, receiverUser, sender, receiver, amount, description, exchanges, timestamp, output);
    }
}
