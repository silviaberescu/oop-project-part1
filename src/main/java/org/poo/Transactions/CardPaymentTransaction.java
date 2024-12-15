package org.poo.Transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;

public final class CardPaymentTransaction extends Transaction {
    private String commerciant;
    private double amount;
    private final String associatedIban;
    public CardPaymentTransaction(final String associatedIban, final int timestamp,
                                  final String description, final double amount,
                                  final String commerciant) {
        super(timestamp, description, associatedIban);
        this.commerciant = commerciant;
        this.amount = amount;
        this.associatedIban = associatedIban;
    }

    /**
     * Converts this card payment transaction to a JSON representation.
     *
     * @return an ObjectNode containing the transaction details,
     *         including the amount and the commerciant.
     */
    @Override
    public ObjectNode toJsonNode() {
        ObjectNode node = super.toJsonNode();
        node.put("amount", amount);
        node.put("commerciant", commerciant);
        return node;
    }

    public String getCommerciant() {
        return commerciant;
    }

    public void setCommerciant(final String commerciant) {
        this.commerciant = commerciant;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public String getAssociatedIban() {
        return associatedIban;
    }
}
