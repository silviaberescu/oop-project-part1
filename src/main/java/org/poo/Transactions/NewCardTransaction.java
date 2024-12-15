package org.poo.Transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;

public final class NewCardTransaction extends Transaction {
    private String cardNumber;
    private String email;
    private String iban;

    public NewCardTransaction(final int timestamp, final String description,
                              final String cardNumber, final String email,
                              final String iban) {
        super(timestamp, description, iban);
        this.cardNumber = cardNumber;
        this.iban = iban;
        this.email = email;
    }

    /**
     * Converts this new card transaction to a JSON representation.
     *
     * @return an ObjectNode containing the transaction details,
     *         including the card number, card holder's email, and
     *         associated account IBAN.
     */
    @Override
    public ObjectNode toJsonNode() {
        ObjectNode node = super.toJsonNode();
        node.put("card", cardNumber);
        node.put("cardHolder", email);
        node.put("account", iban);
        return node;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(final String iban) {
        this.iban = iban;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(final String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
