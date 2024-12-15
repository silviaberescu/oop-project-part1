package org.poo.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Transaction {
    private int timestamp;
    private String description;
    private String associatedIban;

    public Transaction(int timestamp, String description, String associatedIban) {
        this.timestamp = timestamp;
        this.description = description;
        this.associatedIban = associatedIban;
    }

    public ObjectNode toJsonNode() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("timestamp", timestamp);
        node.put("description", description);
        return node;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getAssociatedIban() {
        return associatedIban;
    }

}

