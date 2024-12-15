package org.poo.Transactions;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Transaction {
    private int timestamp;
    private String description;
    private final String associatedIban;

    public Transaction(final int timestamp, final String description,
                       final String associatedIban) {
        this.timestamp = timestamp;
        this.description = description;
        this.associatedIban = associatedIban;
    }

    /**
     * Converts this transaction to a JSON representation.
     *
     * @return an ObjectNode containing the transaction's timestamp and description.
     */
    public ObjectNode toJsonNode() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("timestamp", timestamp);
        node.put("description", description);
        return node;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }

    public final int getTimestamp() {
        return timestamp;
    }

    public final void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    public String getAssociatedIban() {
        return associatedIban;
    }
}

