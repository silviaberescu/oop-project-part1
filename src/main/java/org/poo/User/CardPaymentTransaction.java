package org.poo.User;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class CardPaymentTransaction extends Transaction{
    private String commerciant;
    private double amount;
    private String associatedIban;
    public CardPaymentTransaction(String associatedIban, int timestamp, String description, double amount, String commerciant) {
        super(timestamp, description, associatedIban);
        this.commerciant = commerciant;
        this.amount = amount;
        this.associatedIban = associatedIban;
    }

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

    public void setCommerciant(String commerciant) {
        this.commerciant = commerciant;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAssociatedIban() {
        return associatedIban;
    }
}
