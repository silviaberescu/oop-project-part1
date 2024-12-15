package org.poo.Transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;

public final class DeleteCardTransaction extends Transaction {
    private final String card;
    private String cardHolder;
    private String iban;

    public DeleteCardTransaction(final int timestamp, final String description,
                                 final String card, final String cardHolder,
                                 final String iban) {
        super(timestamp, description, iban);
        this.iban = iban;
        this.cardHolder = cardHolder;
        this.card = card;
    }

    /**
     * Converts this delete card transaction to a JSON representation.
     *
     * @return an ObjectNode containing the transaction details,
     *         including the card number, card holder's information,
     *         and associated account IBAN.
     */
    @Override
    public ObjectNode toJsonNode() {
        ObjectNode node = super.toJsonNode();
        node.put("card", card);
        node.put("cardHolder", cardHolder);
        node.put("account", iban);
        return node;
    }

    public String getCard() {
        return card;
    }

    public String getIBAN() {
        return iban;
    }

    public void setIBAN(final String iban) {
        this.iban = iban;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(final String cardHolder) {
        this.cardHolder = cardHolder;
    }
}
