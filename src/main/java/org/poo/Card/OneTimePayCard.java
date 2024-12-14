package org.poo.Card;

import org.poo.Account.Account;

public class OneTimePayCard extends Card {

    public OneTimePayCard(String associatedAccountIban) {
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
