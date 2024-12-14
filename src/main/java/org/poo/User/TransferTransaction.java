package org.poo.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TransferTransaction extends Transaction {
    private final String senderIBAN;
    private final String receiverIBAN;
    private double amount;
    private boolean sent;
    private String currency;

    public TransferTransaction(int timestamp, String description, String associatedIban, String senderIBAN, boolean sent, double amount,
                               String receiverIBAN, String currency) {
        super(timestamp, description, associatedIban);
        this.senderIBAN = senderIBAN;
        this.sent = sent;
        this.amount = amount;
        this.receiverIBAN = receiverIBAN;
        this.currency = currency;
    }

    @Override
    public ObjectNode toJsonNode() {
        ObjectNode node = super.toJsonNode();
        node.put("senderIBAN", senderIBAN);
        node.put("receiverIBAN", receiverIBAN);
        node.put("amount", amount + " " + currency);
        node.put("transferType", sent ? "sent" : "received");
        return node;
    }

    public String getSenderIBAN() {
        return senderIBAN;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getReceiverIBAN() {
        return receiverIBAN;
    }

}
