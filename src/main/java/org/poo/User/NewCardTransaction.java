package org.poo.User;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class NewCardTransaction extends Transaction {
    private String cardNumber;
    private String email;
    private String IBAN;

    public NewCardTransaction(int timestamp, String description, String cardNumber, String email, String IBAN) {
        super(timestamp, description, IBAN);
        this.cardNumber = cardNumber;
        this.IBAN = IBAN;
        this.email = email;
    }

    @Override
    public ObjectNode toJsonNode() {
        ObjectNode node = super.toJsonNode();
        node.put("card", cardNumber);
        node.put("cardHolder", email);
        node.put("account", IBAN);
        return node;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
