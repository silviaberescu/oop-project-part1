package org.poo.Account;
import org.poo.Card.Card;
import org.poo.utils.Utils;

import java.awt.image.PackedColorModel;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    private double balance;
    private final String currency;
    private final String IBAN;
    private List<Card> cards;
    private double minBalance;
    private String alias;

    public abstract String getType();

    public Account(String currency) {
        this.IBAN = Utils.generateIBAN();
        this.currency = currency;
        this.balance = 0;
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card){
        this.cards.add(card);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public double getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(double minBalance) {
        this.minBalance = minBalance;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getIBAN() {
        return IBAN;
    }

    public String getCurrency() {
        return currency;
    }

    public void removeCard(Card card) {
        this.cards.remove(card);
    }
}


