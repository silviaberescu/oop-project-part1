package org.poo.Transactions;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.Account.Account;

import java.util.ArrayList;

public class SplitPaymentTransaction extends Transaction {
    private final String currency;
    private final double amount;
    private final ArrayList<Account> accounts;
    public SplitPaymentTransaction(final int timestamp, final String description,
                                   final String associatedIban, final String currency,
                                   final double amount, final ArrayList<Account> accounts) {
        super(timestamp, description, associatedIban);
        this.amount = amount;
        this.currency = currency;
        this.accounts = accounts;
    }
    /**
     * Converts the transaction details into a JSON representation.
     * @return an ObjectNode representing the transaction in JSON format.
     * The JSON structure includes:
     * {@code currency}: the currency of the transaction
     * {@code amount}: the total amount of the transaction
     * {@code involvedAccounts}: an array of IBANs for the accounts involved in the transaction
     */
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
