package org.poo.Transactions;

import org.poo.Account.Account;

import java.util.ArrayList;

public final class TransactionData {
    private int timestamp;
    private String description;
    private String associatedIban;
    private String senderIban;
    private String receiverIban;
    private double amount;
    private boolean sent;
    private String currency;
    private ArrayList<Account> accounts;
    private String error;
    private String cardNumber;
    private String email;
    private String commerciant;

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    public String getCommerciant() {
        return commerciant;
    }

    public void setCommerciant(final String commerciant) {
        this.commerciant = commerciant;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(final String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(final ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
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

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public String getReceiverIban() {
        return receiverIban;
    }

    public void setReceiverIban(final String receiverIban) {
        this.receiverIban = receiverIban;
    }

    public String getSenderIban() {
        return senderIban;
    }

    public void setSenderIban(final String senderIban) {
        this.senderIban = senderIban;
    }

    public String getAssociatedIban() {
        return associatedIban;
    }

    public void setAssociatedIban(final String associatedIban) {
        this.associatedIban = associatedIban;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
