package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.Account.Account;
import org.poo.Account.ClassicAccount;
import org.poo.Account.SavingsAccount;
import org.poo.Bank.ExchangeRate;
import org.poo.Card.Card;
import org.poo.Card.ClassicCard;
import org.poo.Card.OneTimePayCard;
import org.poo.User.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class CommandUtils{

    public static void printUsers(final ArrayList<User> users, int timestamp, final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "printUsers");

        ArrayNode userArray = JsonNodeFactory.instance.arrayNode();

        for (User user : users) {
            ObjectNode userNode = createUserJNode(user);
            userArray.add(userNode);
        }

        node.set("output", userArray);
        node.put("timestamp", timestamp);
        output.add(node);
    }
    public static void addAccount(User user, String currency, String type, double interestRate, int timestamp) {
        Account newAccount;
        if(type.equals("savings")){
            newAccount = new SavingsAccount(currency, interestRate);
        } else {
            newAccount = new ClassicAccount(currency);
        }
        user.addAccount(newAccount);
        Transaction transaction = new Transaction(timestamp, "New account created", newAccount.getIBAN());
        user.getTransactions().add(transaction);
    }
    private static ObjectNode createUserJNode(final User user) {
        ObjectNode userNode = JsonNodeFactory.instance.objectNode();
        userNode.put("firstName", user.getFirstName());
        userNode.put("lastName", user.getLastName());
        userNode.put("email", user.getEmail());
        ArrayNode accountArray = JsonNodeFactory.instance.arrayNode();
        if(!user.getAccounts().isEmpty()) {
            for(Account account: user.getAccounts()){
                ObjectNode accountNode = createAccountJNode(account);
                accountArray.add(accountNode);
            }
        }
        userNode.set("accounts", accountArray);
        return userNode;
    }
    private static ObjectNode createAccountJNode(final Account account) {
        ObjectNode accountNode = JsonNodeFactory.instance.objectNode();
        accountNode.put("IBAN", account.getIBAN());
        accountNode.put("balance", account.getBalance());
        accountNode.put("currency", account.getCurrency());
        accountNode.put("type", account.getType());
        ArrayNode cardArray = JsonNodeFactory.instance.arrayNode();
        if(!account.getCards().isEmpty()) {
            for(Card card: account.getCards()){
                ObjectNode cardNode = createCardJNode(card);
                cardArray.add(cardNode);
            }
        }
        accountNode.set("cards", cardArray);
        return accountNode;
    }
    private static ObjectNode createCardJNode(final Card card) {
        ObjectNode cardNode = JsonNodeFactory.instance.objectNode();
        cardNode.put("cardNumber", card.getCardNumber());
        cardNode.put("status", card.getStatus());
        return cardNode;
    }

    public static void createCard(Account account, User user, int timestamp) {
        if(account == null) return;
        Card newCard = new ClassicCard(account.getIBAN());
        account.addCard(newCard);
        //account.addObserver(newCard);
        String description = "New card created";
        NewCardTransaction transaction = new NewCardTransaction(timestamp, description, newCard.getCardNumber(),
                user.getEmail(), account.getIBAN());
        user.addTransaction(transaction);
    }

    public static void addFunds(Account account, double amount) {
        account.setBalance(account.getBalance() + amount);
    }

    public static void createOneTimeCard(User user, Account account, int timestamp) {
        OneTimePayCard newCard = new OneTimePayCard(account.getIBAN());
        account.addCard(newCard);
        String description = "New card created";
        NewCardTransaction transaction = new NewCardTransaction(timestamp, description, newCard.getCardNumber(),
                user.getEmail(), account.getIBAN());
        user.addTransaction(transaction);
    }

    public static void deleteAccount(Account account, User user, int timestamp, final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "deleteAccount");
        try {
            if(account != null) {
                if(account.getBalance() == 0) {
                    account.getCards().clear();
                    user.getAccounts().remove(account);
                    ObjectNode outputNode = JsonNodeFactory.instance.objectNode();
                    outputNode.put("success", "Account deleted");
                    outputNode.put("timestamp", timestamp);
                    node.set("output", outputNode);
                } else {
                    throw new IllegalStateException("Account couldn't be deleted - there are funds remaining");
                }
            } else {
                throw new IllegalStateException("Account does not exist, cannot delete.");
            }
        } catch (Exception e) {
            if (account != null) {
                Transaction transaction = new Transaction(timestamp, e.getMessage(), account.getIBAN());
                user.addTransaction(transaction);
            }
            ObjectNode errorNode = JsonNodeFactory.instance.objectNode();
            errorNode.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
            errorNode.put("timestamp", timestamp);
            node.set("output", errorNode);
        }
        node.put("timestamp", timestamp);
        output.add(node);
    }

    public static void deleteCard(User user, Account account, Card card, int timestamp, ArrayNode output) {
        account.getCards().remove(card);
        DeleteCardTransaction transaction = new DeleteCardTransaction(timestamp, "The card has been destroyed",
                card.getCardNumber(), user.getEmail(), account.getIBAN());
        user.addTransaction(transaction);
    }

    public static void payOnline(User user, Account account, Card card, double amount, String description,
                                 String commerciant, String currency, ArrayList<ExchangeRate> exchanges,
                                 int timestamp, ArrayNode output) {
        if (card != null) {
            double payed = convertAmount(amount, currency, account.getCurrency(), exchanges);
            try {
                if (!card.isFrozen()) {
                    if (account.getBalance() >= payed) {
                        account.setBalance(account.getBalance() - payed);
                        CardPaymentTransaction transaction = new CardPaymentTransaction(account.getIBAN(), timestamp,
                                "Card payment", payed, commerciant);
                        user.addTransaction(transaction);
                        if (card.makePayment()) {
                            if (card.getStatus().equals("inactive")) {
                                deleteCard(user, account, card, timestamp, output);
                                createOneTimeCard(user, account, timestamp);
                            }
                        }
                    } else {
                        throw new IllegalStateException("Insufficient funds");
                    }
                } else {
                    throw new IllegalStateException("The card is frozen");
                }
            } catch (Exception e) {
                Transaction transaction = new Transaction(timestamp, e.getMessage(), account.getIBAN());
                user.addTransaction(transaction);
            }
        } else {
            ObjectNode node = cardNotFoundJsonNode("payOnline", timestamp);
            output.add(node);
        }
    }

    public static void setMinimumBalance(Account account, double amount, int timestamp, ArrayNode output) {
        account.setMinBalance(amount);
    }

    public static void sendMoney(User senderUser, User receiverUser, Account sender, Account receiver, double amount, String description,
                                 ArrayList<ExchangeRate> exchanges, int timestamp, ArrayNode output) {
        if (sender != null && receiver != null) {
            double sent = convertAmount(amount, sender.getCurrency(), receiver.getCurrency(), exchanges);
            try {
                if (sender.getBalance() >= amount) {
                    sender.setBalance(sender.getBalance() - amount);
                    receiver.setBalance(receiver.getBalance() + sent);
                    TransferTransaction transactionSent = new TransferTransaction(timestamp, description, sender.getIBAN(), sender.getIBAN(),
                            true, amount, receiver.getIBAN(), sender.getCurrency());
                    TransferTransaction transactionReceived = new TransferTransaction(timestamp, description, receiver.getIBAN(), sender.getIBAN(),
                            false, sent, receiver.getIBAN(), receiver.getCurrency());
                    senderUser.addTransaction(transactionSent);
                    receiverUser.addTransaction(transactionReceived);
                } else {
                    throw new IllegalStateException("Insufficient funds");
                }
            } catch (Exception e) {
                Transaction transaction = new Transaction(timestamp, e.getMessage(), sender.getIBAN());
                senderUser.addTransaction(transaction);
            }
        }
    }

    public static void setAlias(User user, Account account, String alias, int timestamp) {
        user.addAlias(alias, account);
    }

    public static void printTransactions(User user, int timestamp, ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "printTransactions");
        ArrayNode transactionsArray = JsonNodeFactory.instance.arrayNode();

        for (Transaction transaction : user.getTransactions()) {
            transactionsArray.add(transaction.toJsonNode());
        }
        node.set("output", transactionsArray);
        node.put("timestamp", timestamp);

        output.add(node);
    }

    public static void checkCardStatus(User user, Card card, double balance, double minBalance, int timestamp, ArrayNode output) {
        try {
            String description;
            if (card != null) {
                if (card.getStatus().equals("active") || card.getStatus().equals("warning")) {
                    card.update(balance, minBalance);
                    if(card.isFrozen()) {
                        description = "You have reached the minimum amount of funds, the card will be frozen";
                        Transaction transaction = new Transaction(timestamp, description, null);
                        user.addTransaction(transaction);
                    }
                }
            } else {
                throw new IllegalStateException("Card not found");
            }
        } catch (Exception e) {
            ObjectNode node = cardNotFoundJsonNode("checkCardStatus", timestamp);
            output.add(node);
        }
    }

    private static ObjectNode cardNotFoundJsonNode (String command, int timestamp) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", command);
        ObjectNode outputNode = JsonNodeFactory.instance.objectNode();
        outputNode.put("timestamp", timestamp);
        outputNode.put("description", "Card not found");
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        return node;
    }

    public static double convertAmount(double amount, String fromCurrency, String toCurrency,
                                       ArrayList<ExchangeRate> exchangeRates) {
        double conversedAmount = 0.0;
        if (fromCurrency.equals(toCurrency)) {
            conversedAmount = amount;
            return conversedAmount;
        }
        for (ExchangeRate exchangeRate : exchangeRates) {
            if (exchangeRate.getFrom().equals(fromCurrency) && exchangeRate.getTo().equals(toCurrency)) {
                conversedAmount = amount * exchangeRate.getRate();
                return conversedAmount;
            }
        }
        return conversedAmount;
    }


    public static void splitPayment(ArrayList<User> users, ArrayList<Account> accounts, double amount,
                                    String currency, int timestamp, ArrayList<ExchangeRate> exchangeRates) {
        double splitAmount = amount / accounts.size();
        double[] payments = new double[accounts.size()];
        String description = "Split payment of " + String.format("%.2f", amount) + " " + currency;
        try {
            String accountWithInsufFunds = null;
            for (int i = 0; i < accounts.size(); i++) {
                double payed = convertAmount(splitAmount, currency, accounts.get(i).getCurrency(), exchangeRates);
                if (accounts.get(i).getBalance() <= payed) {
                    accountWithInsufFunds = accounts.get(i).getIBAN();
                }
                payments[i] = payed;
            }
            if (accountWithInsufFunds != null) {
                throw new IllegalStateException("Account " + accountWithInsufFunds + " has insufficient " +
                        "funds for a split payment.");
            }
            for (int i = 0; i < accounts.size(); i++) {
                accounts.get(i).setBalance(accounts.get(i).getBalance() - payments[i]);
                Transaction transaction = new SplitPayementTransaction(timestamp, description,
                        accounts.get(i).getIBAN(), currency, splitAmount, accounts);
                users.get(i).addTransaction(transaction);
            }
        } catch (Exception e) {
            for (int i = 0; i < accounts.size(); i++) {
                Transaction transaction = new SplitPaymentErrorTransaction(timestamp, description,
                        accounts.get(i).getIBAN(), currency, splitAmount, accounts, e.getMessage());
                users.get(i).addTransaction(transaction);
            }
        }
    }

    public static void report(User user, Account account, int startTimestamp, int endTimestamp, int timestamp,
                              ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "report");
        try {
            if (account != null) {
                ObjectNode outputNode = JsonNodeFactory.instance.objectNode();
                outputNode.put("IBAN", account.getIBAN());
                outputNode.put("balance", Double.parseDouble(String.format("%.2f", account.getBalance())));
                outputNode.put("currency", account.getCurrency());
                ArrayNode transactionsArray = JsonNodeFactory.instance.arrayNode();

                for (Transaction transaction : user.getTransactions()) {
                    if (transaction.getTimestamp() >= startTimestamp && transaction.getTimestamp() <= endTimestamp) {
                        if(transaction.getAssociatedIban().equals(account.getIBAN()))
                            transactionsArray.add(transaction.toJsonNode());
                    }
                }
                outputNode.set("transactions", transactionsArray);
                node.set("output", outputNode);
            } else {
                throw new IllegalStateException("Account not found");
            }
        } catch (Exception e) {
            ObjectNode outputNode = JsonNodeFactory.instance.objectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", e.getMessage());
            node.set("output", outputNode);
        }
        node.put("timestamp", timestamp);
        output.add(node);
    }

    public static void spendingsReport(User user, Account account, int startTimestamp, int endTimestamp, int timestamp, ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "spendingsReport");
        try {
            if(account != null) {
                if (!(account instanceof SavingsAccount)) {
                    ObjectNode outputNode = JsonNodeFactory.instance.objectNode();
                    outputNode.put("IBAN", account.getIBAN());
                    outputNode.put("balance", account.getBalance());
                    outputNode.put("currency", account.getCurrency());
                    ArrayNode transactionsArray = JsonNodeFactory.instance.arrayNode();
                    Map<String, Double> commerciantTotals = new HashMap<>();
                    for (Transaction transaction : user.getTransactions()) {
                        if (transaction.getTimestamp() >= startTimestamp && transaction.getTimestamp() <= endTimestamp &&
                                transaction.getDescription().equals("Card payment")) {
                            CardPaymentTransaction cardPaymentTransaction = (CardPaymentTransaction) transaction;
                            if (cardPaymentTransaction.getAssociatedIban().equals(account.getIBAN())) {
                                transactionsArray.add(transaction.toJsonNode());

                                commerciantTotals.put(cardPaymentTransaction.getCommerciant(), commerciantTotals.
                                        getOrDefault(cardPaymentTransaction.getCommerciant(), 0.0) +
                                        cardPaymentTransaction.getAmount());
                            }
                        }
                    }
                    outputNode.set("transactions", transactionsArray);
                    ArrayNode commerciantsArray = JsonNodeFactory.instance.arrayNode();
                    Map<String, Double> sortedCommerciantTotals = new TreeMap<>(commerciantTotals);
                    for (Map.Entry<String, Double> entry : sortedCommerciantTotals.entrySet()) {
                        ObjectNode commerciantNode = JsonNodeFactory.instance.objectNode();
                        commerciantNode.put("commerciant", entry.getKey());
                        commerciantNode.put("total", entry.getValue());
                        commerciantsArray.add(commerciantNode);
                    }
                    outputNode.set("commerciants", commerciantsArray);
                    node.set("output", outputNode);
                } else {
                    ObjectNode errorNode = JsonNodeFactory.instance.objectNode();
                    errorNode.put("error", "This kind of report is not supported for a saving account");
                    node.set("output", errorNode);
                }
            } else {
                throw new IllegalStateException("Account not found");
            }
        } catch (Exception e) {
            ObjectNode outputNode = JsonNodeFactory.instance.objectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", e.getMessage());
            node.set("output", outputNode);
        }
        node.put("timestamp", timestamp);
        output.add(node);
    }

    public static void changeInterest(User user, Account account, double interstRate, int timestamp, ArrayNode output) {
        try {
            if (account.getType().equals("savings")) {
                SavingsAccount savingsAccount = (SavingsAccount) account;
                savingsAccount.setInterestRate(interstRate);
                String description = "Interest rate of the account changed to " + interstRate;
                Transaction transaction = new Transaction(timestamp, description, account.getIBAN());
                user.addTransaction(transaction);
            } else {
                throw new IllegalStateException("This is not a savings account");
            }
        } catch (Exception e) {
            ObjectNode node = notSavingsAccJsonNode("changeInterestRate", timestamp, e.getMessage());
            output.add(node);
        }
    }
    private static ObjectNode notSavingsAccJsonNode (String command, int timestamp, String exception) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", command);
        ObjectNode outputNode = JsonNodeFactory.instance.objectNode();
        outputNode.put("timestamp", timestamp);
        outputNode.put("description", exception);
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        return node;
    }

    public static void addInterest(User user, Account account, int timestamp, ArrayNode output) {
        try {
            if (account.getType().equals("savings")) {
                SavingsAccount savingsAccount = (SavingsAccount) account;
                savingsAccount.setBalance(savingsAccount.getBalance() * (1 + savingsAccount.getInterestRate()));
            } else {
                throw new IllegalStateException("This is not a savings account");
            }
        } catch (Exception e) {
            ObjectNode node = notSavingsAccJsonNode("addInterest", timestamp, e.getMessage());
            output.add(node);
        }
    }
}
