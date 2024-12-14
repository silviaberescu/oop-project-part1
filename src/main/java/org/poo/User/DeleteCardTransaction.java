package org.poo.User;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class DeleteCardTransaction extends Transaction{
    private final String card;
    private String cardHolder;
    private String IBAN;

    public DeleteCardTransaction(int timestamp, String description, String card, String cardHolder, String IBAN) {
        super(timestamp, description, IBAN);
        this.IBAN = IBAN;
        this.cardHolder = cardHolder;
        this.card = card;
    }

    @Override
    public ObjectNode toJsonNode() {
        ObjectNode node = super.toJsonNode();
        node.put("card", card);
        node.put("cardHolder", cardHolder);
        node.put("account", IBAN);
        return node;
    }

    public String getCard() {
        return card;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }
}
