package org.poo.Transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;

public final class TransferTransaction extends Transaction {
    private final String senderIBAN;
    private final String receiverIBAN;
    private double amount;
    private boolean sent;
    private final String currency;

    public TransferTransaction(final int timestamp, final String description,
                               final String associatedIban, final String senderIBAN,
                               final boolean sent, final double amount,
                               final String receiverIBAN, final String currency) {
        super(timestamp, description, associatedIban);
        this.senderIBAN = senderIBAN;
        this.sent = sent;
        this.amount = amount;
        this.receiverIBAN = receiverIBAN;
        this.currency = currency;
    }

    /**
     * Converts this transaction to a JSON representation.
     *
     * @return an ObjectNode containing the transaction details,
     *         including sender and receiver IBANs, amount, currency,
     *         and transfer type (sent or received).
     */
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

    public void setSent(final boolean sent) {
        this.sent = sent;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public String getReceiverIBAN() {
        return receiverIBAN;
    }

}
