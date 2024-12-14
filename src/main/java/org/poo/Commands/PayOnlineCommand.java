package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.Bank.ExchangeRate;
import org.poo.Card.Card;
import org.poo.User.User;

import java.util.ArrayList;

public class PayOnlineCommand implements Command{
    private String cardNumber;
    private double amount;
    private String currency;
    private String description;
    private String commerciant;
    private int timestamp;
    private User user;
    ArrayList<ExchangeRate> exchanges;

    public PayOnlineCommand(User user, String cardNumber, double amount, String description,
                            String commerciant, String currency, int timestamp,
                            ArrayList<ExchangeRate> exchanges) {
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
    public void execute(ArrayNode output) {
        boolean found = false;
        for(Account accountCurrent: user.getAccounts()){
            for(Card cardCurrent: accountCurrent.getCards()){
                if(cardCurrent.getCardNumber().equals(cardNumber)){
                    CommandUtils.payOnline(user, accountCurrent, cardCurrent, amount, description, commerciant,
                            currency, exchanges, timestamp, output);
                    found = true;
                    break;
                }
            }
        }
        if(!found)
            CommandUtils.payOnline(user, null, null, amount, description, commerciant,
                currency, exchanges, timestamp, output);
    }
}
