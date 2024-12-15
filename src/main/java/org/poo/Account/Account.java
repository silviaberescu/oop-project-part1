package org.poo.Account;
import org.poo.Card.Card;
import org.poo.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    private double balance;
    private final String currency;
    private final String iban;
    private List<Card> cards;
    private double minBalance;
    private String alias;

    /**
     * Retrieves the type of the account
     *
     * @return a String representing the type of the object.
     */
    public abstract String getType();

    public Account(final String currency) {
        this.iban = Utils.generateIBAN();
        this.currency = currency;
        this.balance = 0;
        this.cards = new ArrayList<>();
    }

    /**
     * Adds a card to the list of cards
     *
     * @param card the Card object to be added.
     */
    public final void addCard(final Card card) {
        this.cards.add(card);
    }

    public final double getBalance() {
        return balance;
    }

    public final void setBalance(final double balance) {
        this.balance = balance;
    }

    public final String getAlias() {
        return alias;
    }

    public final void setAlias(final String alias) {
        this.alias = alias;
    }

    public final double getMinBalance() {
        return minBalance;
    }

    public final void setMinBalance(final double minBalance) {
        this.minBalance = minBalance;
    }

    public final List<Card> getCards() {
        return cards;
    }

    public final void setCards(final List<Card> cards) {
        this.cards = cards;
    }

    public final String getIBAN() {
        return iban;
    }

    public final String getCurrency() {
        return currency;
    }
}


