package org.poo.Transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.Account.Account;

import java.util.ArrayList;

public class SplitPaymentErrorTransaction extends SplitPaymentTransaction {
    private final String error;

    public SplitPaymentErrorTransaction(final int timestamp, final String description,
                                        final String associatedIban, final String currency,
                                        final double amount, final ArrayList<Account> accounts,
                                        final String error) {
        super(timestamp, description, associatedIban, currency, amount, accounts);
        this.error = error;
    }

    /**
     * Converts this transaction to a JSON representation.
     *
     * @return an ObjectNode containing the transaction details,
     *         including the error message.
     */
    @Override
    public ObjectNode toJsonNode() {
        ObjectNode node = super.toJsonNode();
        node.put("error", error);
        return node;
    }
}
