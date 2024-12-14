package org.poo.Account;

public class ClassicAccount extends Account {
    public ClassicAccount(String currency) {
        super(currency);
    }

    @Override
    public String getType() {
        return "classic";
    }
}
