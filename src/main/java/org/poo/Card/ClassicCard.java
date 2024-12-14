package org.poo.Card;

public class ClassicCard extends Card {

    public ClassicCard(String associatedAccountIban) {
        super(associatedAccountIban);
    }

    @Override
    public boolean makePayment() {
        return getStatus().equals("active") || getStatus().equals("warning");
    }
}
