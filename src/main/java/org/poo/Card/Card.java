package org.poo.Card;

import org.poo.utils.Utils;

public abstract class Card {
    private final String cardNumber;
    private String status; // active, frozen, warning, inactive
    private final String associatedIban;

    public Card(String associatedIban) {
        this.cardNumber = Utils.generateCardNumber();
        this.associatedIban = associatedIban;
        this.status = "active";
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getAssociatedIban() {
        return associatedIban;
    }

    public void deactivate() {
        this.status = "inactive";
    }
    public void setFrozen() {
        this.status = "frozen";
    }
    public boolean isFrozen() {
        return status.equals("frozen");

    }
    public void setWarning() {
        this.status = "warning";
    }

   // @Override
    public void update(double currentBalance, double minimumBalance) {
        if (currentBalance <= minimumBalance) {
            setFrozen();
        } else if (currentBalance - minimumBalance <= 30) {
            setWarning();
        }
    }

    // Abstract method for handling payments
    public abstract boolean makePayment();
}

