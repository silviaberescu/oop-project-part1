package org.poo.Account;

public final class ClassicAccount extends Account {
    public ClassicAccount(final String currency) {
        super(currency);
    }

    @Override
    public String getType() {
        return "classic";
    }
}
