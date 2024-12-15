package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.Bank.ExchangeRate;
import org.poo.Card.Card;
import org.poo.User.User;

import java.util.ArrayList;

public final class PayOnlineCommand implements Command {
    private final String cardNumber;
    private final double amount;
    private final String currency;
    private final String description;
    private final String commerciant;
    private final int timestamp;
    private final User user;
    private final ArrayList<ExchangeRate> exchanges;

    public PayOnlineCommand(final User user, final String cardNumber,
                            final double amount, final String description,
                            final String commerciant, final String currency,
                            final int timestamp,
                            final ArrayList<ExchangeRate> exchanges) {
        this.user = user;
        this.timestamp = timestamp;
        this.commerciant = commerciant;
        this.description = description;
        this.currency = currency;
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.exchanges = exchanges;
    }


    @Override
    public void execute(final ArrayNode output) {
        boolean found = false;
        for (Account accountCurrent: user.getAccounts()) {
            for (Card cardCurrent: accountCurrent.getCards()) {
                if (cardCurrent.getCardNumber().equals(cardNumber)) {
                    CommandUtils.payOnline(user, accountCurrent, cardCurrent,
                            amount, commerciant, currency, exchanges, timestamp,
                            output, description);
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            CommandUtils.payOnline(user, null, null, amount,
                    commerciant, currency, exchanges, timestamp, output,
                    description);
        }
    }
}
