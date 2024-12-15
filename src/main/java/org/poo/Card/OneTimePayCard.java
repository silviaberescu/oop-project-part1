package org.poo.Card;

public final class OneTimePayCard extends Card {

    public OneTimePayCard(final String associatedAccountIban) {
        super(associatedAccountIban);
    }
    @Override
    public boolean makePayment() {
        if (this.getStatus().equals("active") || getStatus().equals("warning")) {
            deactivate();
            return true;
        } else {
            return false;
        }
    }
}
