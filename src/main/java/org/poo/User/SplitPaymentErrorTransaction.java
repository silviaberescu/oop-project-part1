package org.poo.User;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.Account.Account;

import java.util.ArrayList;

public class SplitPaymentErrorTransaction extends SplitPayementTransaction {
    private String error;

    public SplitPaymentErrorTransaction(int timestamp, String description, String associatedIban, String currency, double amount,
                                        ArrayList<Account> accounts, String error) {
        super(timestamp, description, associatedIban, currency, amount, accounts);
        this.error = error;
    }
    @Override
    public ObjectNode toJsonNode() {
        ObjectNode node = super.toJsonNode();
        node.put("error", error);
        return node;
    }
}
