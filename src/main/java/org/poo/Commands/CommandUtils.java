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
import org.poo.Transactions.CardPaymentTransaction;
import org.poo.Transactions.Transaction;
import org.poo.Transactions.TransactionFactory;
import org.poo.Transactions.TransactionData;
import org.poo.User.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import static org.poo.Commands.ObjectToJsonConverter.cardNotFoundJsonNode;
import static org.poo.Commands.ObjectToJsonConverter.createUserJNode;
import static org.poo.Commands.ObjectToJsonConverter.notSavingsAccJsonNode;



public final class CommandUtils {

    private CommandUtils() {
    }
    /**
     * Adds to the output a list of users as a JSON object
     * Each user is represented as a JSON node, which is added to an array
     *
     * @param users      an ArrayList of User objects to be included
     * @param timestamp  an integer representing the timestamp to be added to the output
     * @param output     an ArrayNode to which the formatted JSON output will be appended
     */
    public static void printUsers(final ArrayList<User> users, final int timestamp,
                                  final ArrayNode output) {
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
    /**
     * Creates a new account for a user based on the specified type and currency,
     * associates it with the user, and logs a transaction.
     *
     * @param user          the user to whom the new account will be added
     * @param currency      a String representing the currency of the account
     * @param type          a String indicating the type of account
     * @param interestRate  specifies the interest rate
     * @param timestamp     represents the timestamp of the account creation
     */
    public static void addAccount(final User user, final String currency,
                                  final String type, final double interestRate,
                                  final int timestamp) {
        Account newAccount;
        if (type.equals("savings")) {
            newAccount = new SavingsAccount(currency, interestRate);
        } else {
            newAccount = new ClassicAccount(currency);
        }
        user.addAccount(newAccount);
        Transaction transaction = createGenericTransaction(timestamp,
                "New account created", newAccount.getIBAN());
        user.getTransactions().add(transaction);
    }
    /**
     * Creates a new card for a specified account and logs it as a transaction
     *
     * @param account   the account associated with the card being created
     * @param user      the user for whom the card is being created
     * @param timestamp represents the timestamp of the card creation
     */
    public static void createCard(final Account account, final User user, final int timestamp) {
        if (account == null) {
            return;
        }
        Card newCard = new ClassicCard(account.getIBAN());
        createCardAddTransaction(newCard, account, user, timestamp);
    }

    /**
     * Adds funds to the specified account
     *
     * @param account the account to which the funds will be added
     * @param amount  represents the amount to be added to the account's balance
     */
    public static void addFunds(final Account account, final double amount) {
        account.setBalance(account.getBalance() + amount);
    }

    /**
     * Creates a one-time use card associated to the specified account,
     * and creates a specific transaction.
     *
     * @param user      the user for whom the one-time use card is being created
     * @param account   the account associated with the card
     * @param timestamp represents the timestamp of the card creation
     */
    public static void createOneTimeCard(final User user, final Account account,
                                         final int timestamp) {
        OneTimePayCard newCard = new OneTimePayCard(account.getIBAN());
        createCardAddTransaction(newCard, account, user, timestamp);
    }

    /**
     * Deletes the specified account for a user if the balance is 0 and logs the result
     * If the deletion is successful, all associated cards are cleared, and the account is removed
     * If the account cannot be deleted, an error message is used to create a specific transaction
     *
     * @param account   the account to be deleted
     * @param user      the user who owns the account being deleted
     * @param timestamp represents the timestamp of the deletion command
     * @param output    an ArrayNode to which the result of the operation is added
     */
    public static void deleteAccount(final Account account, final User user,
                                     final int timestamp, final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "deleteAccount");
        try {
            if (account != null) {
                if (account.getBalance() == 0) {
                    account.getCards().clear();
                    user.getAccounts().remove(account);
                    ObjectNode outputNode = JsonNodeFactory.instance.objectNode();
                    outputNode.put("success", "Account deleted");
                    outputNode.put("timestamp", timestamp);
                    node.set("output", outputNode);
                } else {
                    throw new IllegalStateException("Account couldn't be deleted - "
                            + "there are funds remaining");
                }
            } else {
                throw new IllegalStateException("Account does not exist, cannot delete.");
            }
        } catch (Exception e) {
            if (account != null) {
                TransactionData data = new TransactionData();
                data.setTimestamp(timestamp);
                data.setDescription(e.getMessage());
                data.setAssociatedIban(account.getIBAN());
                Transaction transaction = TransactionFactory.createTransaction(TransactionFactory.
                        TransactionType.GENERIC, data);
                user.addTransaction(transaction);
            }
            ObjectNode errorNode = JsonNodeFactory.instance.objectNode();
            errorNode.put("error", "Account couldn't be deleted - see "
                    + "org.poo.transactions for details");
            errorNode.put("timestamp", timestamp);
            node.set("output", errorNode);
        }
        node.put("timestamp", timestamp);
        output.add(node);
    }

    /**
     * Deletes a specified card associated with a user and an account, and creates a transaction
     *
     * @param user      the user who owns the card being deleted
     * @param account   the account associated with the card
     * @param card      the card to be removed.
     * @param timestamp represents the timestamp of the deletion action
     */
    public static void deleteCard(final User user, final Account account, final Card card,
                                  final int timestamp) {
        account.getCards().remove(card);

        String description = "The card has been destroyed";
        TransactionData data = new TransactionData();
        data.setTimestamp(timestamp);
        data.setDescription(description);
        data.setCardNumber(card.getCardNumber());
        data.setEmail(user.getEmail());
        data.setAssociatedIban(account.getIBAN());

        Transaction transaction = TransactionFactory.createTransaction(TransactionFactory.
                TransactionType.DELETE_CARD, data);

        user.addTransaction(transaction);
    }

    /**
     * Processes an online payment using a specified card and account
     * Converts the payment amount to the account's currency, deducts from the balance,
     * and creates a specific transaction. If the card is a one-time-use card it becomes inactive
     * after the payment and is replaced with a new one. It handles errors such as insufficient
     * funds or frozen cards.
     *
     * @param user         the user initiating the payment.
     * @param account      the account from which the payment will be deducted
     * @param card         the card used for the payment. If it is null an error
     *                     appears in the output.
     * @param amount       the amount of the payment in the specified currency
     * @param description  describes the payment purpose.
     * @param commerciant  represents the commerciant receiving the payment
     * @param currency     indicates the currency of the payment
     * @param exchanges    an ArrayList ExchangeRate objects for currency conversion
     * @param timestamp    represents the timestamp of the payment
     * @param output       an ArrayNode to store the result of the operation
     *
     */
    public static void payOnline(final User user, final Account account,
                                 final Card card, final double amount,
                                 final String commerciant, final String currency,
                                 final ArrayList<ExchangeRate> exchanges,
                                 final int timestamp, final ArrayNode output,
                                 final String description) {
        if (card != null) {
            double payed = convertAmount(amount, currency, account.getCurrency(), exchanges);
            try {
                if (!card.isFrozen()) {
                    if (account.getBalance() >= payed) {
                        account.setBalance(account.getBalance() - payed);

                        TransactionData data = new TransactionData();
                        data.setTimestamp(timestamp);
                        data.setDescription("Card payment");
                        data.setAssociatedIban(account.getIBAN());
                        data.setAmount(payed);
                        data.setCommerciant(commerciant);
                        Transaction transaction = TransactionFactory.createTransaction(
                                TransactionFactory.TransactionType.CARD_PAYMENT, data);

                        user.addTransaction(transaction);

                        if (card.makePayment()) {
                            if (card.getStatus().equals("inactive")) {
                                deleteCard(user, account, card, timestamp);
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
                Transaction transaction = createGenericTransaction(timestamp,
                        e.getMessage(), account.getIBAN());
                user.addTransaction(transaction);
            }
        } else {
            ObjectNode node = cardNotFoundJsonNode("payOnline", timestamp);
            output.add(node);
        }
    }

    /**
     * Sets the minimum balance for the specified account
     *
     * @param account the account for which the minimum balance is being set
     * @param amount  the value representing the minimum balance to be set
     */
    public static void setMinimumBalance(final Account account, final double amount) {
        account.setMinBalance(amount);
    }

    /**
     * Transfers money from the sender's account to the receiver's account, converting the amount
     * to the receiver's currency, and creates specific transactions for both users. If the sender
     * has insufficient funds it is considered an error.
     *
     * @param senderUser   the user sending the money
     * @param receiverUser the user receiving the money
     * @param sender       the account from which the money is being sent
     * @param receiver     the account to which the money is being received
     * @param amount       the amount of money to be sent
     * @param description  a String describing the purpose of the transfer
     * @param exchanges    an ArrayList of ExchangeRate objects used for currency conversion
     * @param timestamp    represents the timestamp of the transfer
     *
     */
    public static void sendMoney(final User senderUser, final User receiverUser,
                                 final Account sender, final Account receiver,
                                 final double amount, final String description,
                                 final ArrayList<ExchangeRate> exchanges,
                                 final int timestamp) {
        if (sender != null && receiver != null) {
            double sent = convertAmount(amount, sender.getCurrency(), receiver.getCurrency(),
                    exchanges);
            try {
                if (sender.getBalance() >= amount) {
                    sender.setBalance(sender.getBalance() - amount);
                    receiver.setBalance(receiver.getBalance() + sent);

                    TransactionData senderData = createTransferData(timestamp, description,
                            sender.getIBAN(), sender.getIBAN(), true, amount,
                            receiver.getIBAN(), sender.getCurrency());
                    TransactionData receiverData = createTransferData(timestamp, description,
                            receiver.getIBAN(), sender.getIBAN(), false, sent,
                            receiver.getIBAN(), receiver.getCurrency());

                    Transaction senderTransaction = TransactionFactory.createTransaction(
                            TransactionFactory.TransactionType.TRANSFER, senderData);
                    Transaction receiverTransaction = TransactionFactory.createTransaction(
                            TransactionFactory.TransactionType.TRANSFER, receiverData);

                    senderUser.addTransaction(senderTransaction);
                    receiverUser.addTransaction(receiverTransaction);
                } else {
                    throw new IllegalStateException("Insufficient funds");
                }
            } catch (Exception e) {
                Transaction transaction = createGenericTransaction(timestamp, e.getMessage(),
                        sender.getIBAN());
                senderUser.addTransaction(transaction);
            }
        }
    }

    /**
     * Sets an alias for the specified account. The alias is stored in the user's alias list
     *
     * @param user   the user to whom the account belongs
     * @param account the account to which the alias is being set
     * @param alias  represents the alias
     */
    public static void setAlias(final User user, final Account account, final String alias) {
        user.addAlias(alias, account);
    }

    /**
     * Prints the list of transactions associated to the specified user to the output
     * Each transaction is represented as a JSON node and is
     *
     * @param user      the user whose transactions will be printed
     * @param timestamp represents the timestamp of the operation
     * @param output    an ArrayNode where the result will be added
     */
    public static void printTransactions(final User user, final int timestamp,
                                         final ArrayNode output) {
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

    /**
     * Checks the status of the specified card and updates its state based
     * on the account balance and minimum balance. If the card reaches the
     * minimum balance threshold, it is frozen, and a transaction is made.
     * If the card is not found an error message is added to the output.
     *
     * @param user      the user associated with the card
     * @param card      the card whose status is being checked
     * @param balance   the current balance of the user's account
     * @param minBalance the minimum balance
     * @param timestamp the timestamp of the operation.
     * @param output    an arrayNode to store the result of the operation
     *
     */
    public static void checkCardStatus(final User user, final Card card, final double balance,
                                       final double minBalance, final int timestamp,
                                       final ArrayNode output) {
        try {
            String description;
            if (card != null) {
                if (card.getStatus().equals("active") || card.getStatus().equals("warning")) {
                    card.update(balance, minBalance);
                    if (card.isFrozen()) {
                        description = "You have reached the minimum amount of funds, the card will "
                                + "be frozen";
                        Transaction transaction = createGenericTransaction(timestamp, description,
                                null);
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

    /**
     * Creates a classic transaction with the specified timestamp, description,
     * and associated IBAN.
     *
     * @param timestamp       the timestamp of the transaction
     * @param description     a String describing the transaction
     * @param associatedIban  the IBAN associated with the transaction
     *
     * @return a Transaction object representing the new transaction
     */
    public static Transaction createGenericTransaction(final int timestamp,
                                                        final String description,
                                                        final String associatedIban) {
        TransactionData data = new TransactionData();
        data.setTimestamp(timestamp);
        data.setDescription(description);
        data.setAssociatedIban(associatedIban);
        return TransactionFactory.createTransaction(
                TransactionFactory.TransactionType.GENERIC, data);
    }

    /**
     * Creates a TransactionData object for a transfer transaction with the specified details
     *
     * @param timestamp         the timestamp of the transaction
     * @param description       a String describing the transaction
     * @param associatedIban    the associated IBAN of the transaction
     * @param senderIban        a String representing the IBAN of the sender
     * @param sent              indicates if the transaction is sent or received.
     * @param amount            the amount involved in the transaction
     * @param receiverIban      the IBAN of the receiver.
     * @param currency          the currency of the transaction.
     *
     */
    public static TransactionData createTransferData(final int timestamp, final String description,
                                                     final String associatedIban,
                                                     final String senderIban,
                                                     final boolean sent, final double amount,
                                                     final String receiverIban,
                                                     final String currency) {
        TransactionData data = new TransactionData();
        data.setTimestamp(timestamp);
        data.setDescription(description);
        data.setAssociatedIban(associatedIban);
        data.setSenderIban(senderIban);
        data.setReceiverIban(receiverIban);
        data.setAmount(amount);
        data.setCurrency(currency);
        data.setSent(sent);

        return data;
    }

    /**
     * Converts a given amount of money from one currency to another based on
     * the provided exchange rates. If the source and destination currencies
     * are the same, the method simply returns the original amount, otherwise
     * it calculates and returns the converted amount.
     *
     * @param amount        the amount to be converted
     * @param fromCurrency  the currency to convert from
     * @param toCurrency    the currency to convert to
     * @param exchangeRates a list of ExchangeRate objects containing exchange rates
     *
     * @return the converted amount in the target currency
     */
    public static double convertAmount(final double amount, final String fromCurrency,
                                       final String toCurrency,
                                       final ArrayList<ExchangeRate> exchangeRates) {
        double conversedAmount = 0.0;
        if (fromCurrency.equals(toCurrency)) {
            conversedAmount = amount;
            return conversedAmount;
        }
        for (ExchangeRate exchangeRate : exchangeRates) {
            if (exchangeRate.getFrom().equals(fromCurrency) && exchangeRate.getTo().
                    equals(toCurrency)) {
                conversedAmount = amount * exchangeRate.getRate();
                return conversedAmount;
            }
        }
        return conversedAmount;
    }

    /**
     * Splits a payment of a given amount among a list of accounts, and
     * updates each account's balance. If there are sufficient funds,
     * the payment is distributed equally across the accounts, and a
     * transaction is created for each account. If there are insufficient funds,
     * the payment fails, and a transaction is created for each account
     * indicating the error.
     *
     * @param users          a list of user objects associated with the accounts
     * @param accounts       a list of account objects to which the payment will be split
     * @param amount         the total amount to be split among the accounts
     * @param currency       the currency in which the payment is made
     * @param timestamp      the timestamp of the payment
     * @param exchangeRates  a list ExchangeRate objects for converting sums
     */
    public static void splitPayment(final ArrayList<User> users, final ArrayList<Account> accounts,
                                    final double amount, final String currency,
                                    final int timestamp,
                                    final ArrayList<ExchangeRate> exchangeRates) {
        double splitAmount = amount / accounts.size();
        double[] payments = new double[accounts.size()];
        String description = "Split payment of " + String.format("%.2f", amount) + " " + currency;
        try {
            checkSufficientFunds(accounts, splitAmount, currency, exchangeRates, payments);
            for (int i = 0; i < accounts.size(); i++) {
                accounts.get(i).setBalance(accounts.get(i).getBalance() - payments[i]);
                Transaction transaction = createSplitPaymentTransaction(
                        timestamp, description, accounts.get(i), currency, splitAmount,
                        accounts, null);
                users.get(i).addTransaction(transaction);
            }
        } catch (Exception e) {
            for (int i = 0; i < accounts.size(); i++) {
                Transaction transaction = createSplitPaymentTransaction(
                        timestamp, description, accounts.get(i), currency, splitAmount,
                        accounts, e.getMessage());
                users.get(i).addTransaction(transaction);
            }
        }
    }

    /**
     * Checks if each account in the provided list has sufficient funds to
     * cover a split payment. For each account, it calculates the amount to
     * be paid and compares it with the account's balance. If any account has
     * insufficient funds, an exception is thrown with the relevant account's IBAN.
     * The method also updates the provided payments array with the amounts to
     * be paid for each account.
     *
     * @param accounts       a list of account objects
     * @param splitAmount    the amount to be split among the accounts
     * @param currency       the currency in which the payment is made
     * @param exchangeRates  a list of ExchangeRate objects to convert
     *                       the split amount
     * @param payments       an array that will hold the amounts to be
     *                       paid for each account
     *
     */
    private static void checkSufficientFunds(final ArrayList<Account> accounts,
                                             final double splitAmount, final String currency,
                                             final ArrayList<ExchangeRate> exchangeRates,
                                             final double[] payments) {
        String accountWithInsufFunds = null;
        for (int i = 0; i < accounts.size(); i++) {
            double payed = convertAmount(splitAmount, currency, accounts.get(i).getCurrency(),
                    exchangeRates);
            if (accounts.get(i).getBalance() <= payed) {
                accountWithInsufFunds = accounts.get(i).getIBAN();
            }
            payments[i] = payed;
        }
        if (accountWithInsufFunds != null) {
            throw new IllegalStateException("Account " + accountWithInsufFunds
                    + " has insufficient funds for a split payment.");
        }
    }

    /**
     * Creates a split payment transaction or an error transaction
     * based on whether an error message is provided. The transaction
     * is created using the specified details. If no error is present,
     * a regular split payment transaction is created, otherwise, an
     * error transaction is generated.
     *
     * @param timestamp      the timestamp of the transaction
     * @param description    a description of the transaction
     * @param account        the account associated with the transaction
     * @param currency       the currency of the transaction
     * @param splitAmount    the amount to be paid
     * @param accounts       a list Account objects involved in the split payment
     * @param error          an error message related to the transaction
     * @return               a Transaction object representing the split payment or
     *                       error transaction
     */
    private static Transaction createSplitPaymentTransaction(final int timestamp,
                                                             final String description,
                                                             final Account account,
                                                             final String currency,
                                                             final double splitAmount,
                                                             final ArrayList<Account> accounts,
                                                             final String error) {
        TransactionData data = new TransactionData();
        data.setTimestamp(timestamp);
        data.setDescription(description);
        data.setAssociatedIban(account.getIBAN());
        data.setCurrency(currency);
        data.setAmount(splitAmount);
        data.setAccounts(accounts);
        data.setError(error);
        if (error == null) {
            return TransactionFactory.createTransaction(
                    TransactionFactory.TransactionType.SPLIT_PAYMENT, data);
        } else {
            return TransactionFactory.createTransaction(
                    TransactionFactory.TransactionType.SPLIT_PAYMENT_ERROR, data);
        }
    }

    /**
     * Generates a report for the specified account, which includes all transactions within
     * the specified timestamp range. If the account is not found an error message will be
     * included.
     *
     * @param user            the User that holds the account
     * @param account         the Account object for which the report is generated
     * @param startTimestamp  the start timestamp of the range
     * @param endTimestamp    the end timestamp of the range
     * @param timestamp       the timestamp of when the report is generated
     * @param output          the ArrayNode to which the generated report will be added
     */
    public static void report(final User user, final Account account, final int startTimestamp,
                              final int endTimestamp, final int timestamp,
                              final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "report");
        try {
            if (account != null) {
                ObjectNode outputNode = JsonNodeFactory.instance.objectNode();
                outputNode.put("IBAN", account.getIBAN());
                outputNode.put("balance", account.getBalance());
                outputNode.put("currency", account.getCurrency());
                ArrayNode transactionsArray = JsonNodeFactory.instance.arrayNode();

                for (Transaction transaction : user.getTransactions()) {
                    if (transaction.getTimestamp() >= startTimestamp && transaction.
                            getTimestamp() <= endTimestamp) {
                        if (transaction.getAssociatedIban().equals(account.getIBAN())) {
                            transactionsArray.add(transaction.toJsonNode());
                        }

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

    /**
     * Generates a report of all card payment transactions for a given account
     * within a specified timestamp range. The report calculates and lists the
     * total spend for each commerciant involved.
     * An error message will be returned in case the account is a savings one.
     *
     * @param user            the user object for whom the report is generated
     * @param account         the account for which the spendings report is generated
     * @param startTimestamp  the start timestamp of the range
     * @param endTimestamp    the end timestamp of the range
     * @param timestamp       the timestamp of when the report is generated
     * @param output          the ArrayNode to which the report will be added
     */
    public static void spendingsReport(final User user, final Account account,
                                       final int startTimestamp, final int endTimestamp,
                                       final int timestamp, final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "spendingsReport");
        try {
            if (account != null) {
                if (!(account instanceof SavingsAccount)) {
                    ObjectNode outputNode = JsonNodeFactory.instance.objectNode();
                    outputNode.put("IBAN", account.getIBAN());
                    outputNode.put("balance", account.getBalance());
                    outputNode.put("currency", account.getCurrency());
                    ArrayNode transactionsArray = JsonNodeFactory.instance.arrayNode();
                    Map<String, Double> commerciantTotals = new HashMap<>();
                    for (Transaction transaction : user.getTransactions()) {
                        if (transaction.getTimestamp() >= startTimestamp && transaction.
                                getTimestamp() <= endTimestamp
                                && transaction.getDescription().equals("Card payment")) {
                            CardPaymentTransaction cardPaymentTransaction =
                                    (CardPaymentTransaction) transaction;
                            if (cardPaymentTransaction.getAssociatedIban().
                                    equals(account.getIBAN())) {
                                transactionsArray.add(transaction.toJsonNode());

                                commerciantTotals.put(cardPaymentTransaction.getCommerciant(),
                                        commerciantTotals.getOrDefault(cardPaymentTransaction.
                                                getCommerciant(), 0.0)
                                                + cardPaymentTransaction.getAmount());
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
                    errorNode.put("error", "This kind of report is not "
                            + "supported for a saving account");
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

    /**
     * Changes the interest rate of a given savings account and records the transaction.
     * If the account is not a savings account, an error is produced.
     *
     * @param user          the user who owns the account and to whom the transaction
     *                      will be recorded
     * @param account       the account whose interest rate will be changed
     * @param interstRate   the new interest rate
     * @param timestamp     the timestamp of when the interest rate change occurs
     * @param output        the ArrayNode to which the error will be added
     */
    public static void changeInterest(final User user, final Account account,
                                      final double interstRate, final int timestamp,
                                      final ArrayNode output) {
        try {
            if (account.getType().equals("savings")) {
                SavingsAccount savingsAccount = (SavingsAccount) account;
                savingsAccount.setInterestRate(interstRate);
                String description = "Interest rate of the account changed to " + interstRate;
                Transaction transaction = createGenericTransaction(timestamp, description,
                        account.getIBAN());
                user.addTransaction(transaction);
            } else {
                throw new IllegalStateException("This is not a savings account");
            }
        } catch (Exception e) {
            ObjectNode node = notSavingsAccJsonNode("changeInterestRate", timestamp,
                    e.getMessage());
            output.add(node);
        }
    }

    /**
     * Adds interest to a given savings account based on its current balance and interest rate.
     * If the provided account is not a savings account, an error is recorded in the output.
     *
     * @param account   the account to which the interest will be added
     * @param timestamp the timestamp of when the interest is applied
     * @param output    the ArrayNode to which the error message will be added
     */
    public static void addInterest(final Account account, final int timestamp,
                                   final ArrayNode output) {
        try {
            if (account.getType().equals("savings")) {
                SavingsAccount savingsAccount = (SavingsAccount) account;
                savingsAccount.setBalance(savingsAccount.getBalance() * (1 + savingsAccount.
                        getInterestRate()));
            } else {
                throw new IllegalStateException("This is not a savings account");
            }
        } catch (Exception e) {
            ObjectNode node = notSavingsAccJsonNode("addInterest", timestamp, e.getMessage());
            output.add(node);
        }
    }

    /**
     * Creates a new card transaction for the given account, adds the card to
     * the account's list of cards, and generates a transaction associated with
     * the card creation. The transaction is then added to the user's transaction
     * history.
     *
     * @param newCard  the card that is being created and added to the account
     * @param account  the account to which the new card is being added
     * @param user     the user who owns the account
     * @param timestamp the timestamp when the card is created
     */
    public static void createCardAddTransaction(final Card newCard, final Account account,
                                                final User user, final int timestamp) {
        account.addCard(newCard);

        String description = "New card created";

        TransactionData data = new TransactionData();
        data.setTimestamp(timestamp);
        data.setDescription(description);
        data.setCardNumber(newCard.getCardNumber());
        data.setEmail(user.getEmail());
        data.setAssociatedIban(account.getIBAN());

        Transaction transaction = TransactionFactory.createTransaction(
                TransactionFactory.TransactionType.NEW_CARD, data);

        user.addTransaction(transaction);
    }
}
