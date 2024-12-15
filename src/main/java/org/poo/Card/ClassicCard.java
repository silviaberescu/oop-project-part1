package org.poo.Card;

public final class ClassicCard extends Card {

    public ClassicCard(final String associatedAccountIban) {
        super(associatedAccountIban);
    }

    @Override
    public boolean makePayment() {
        return getStatus().equals("active") || getStatus().equals("warning");
    }
}
