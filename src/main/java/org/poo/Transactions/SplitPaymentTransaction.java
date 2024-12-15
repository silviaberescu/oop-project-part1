package org.poo.User;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.Account.Account;
import org.poo.Transactions.Transaction;

import java.util.ArrayList;

public class SplitPayementTransaction extends Transaction {
    private String currency;
    private double amount;
    private ArrayList<Account> accounts;
    public SplitPayementTransaction(int timestamp, String description, String associatedIban, String currency,
                                    double amount, ArrayList<Account> accounts) {
        super(timestamp, description, associatedIban);
        this.amount = amount;
        this.currency = currency;
        this.accounts = accounts;
    }

    @Override
    public ObjectNode toJsonNode() {
        ObjectNode node = super.toJsonNode();
        node.put("currency", currency);
        node.put("amount", amount);
        ArrayNode involvedAccounts = JsonNodeFactory.instance.arrayNode();
        for (Account account : accounts) {
            involvedAccounts.add(account.getIBAN());
        }
        node.set("involvedAccounts", involvedAccounts);
        return node;
    }
}
