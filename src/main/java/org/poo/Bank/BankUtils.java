package org.poo.Bank;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.Card.Card;
import java.util.HashMap;
import org.poo.Commands.Command;
import org.poo.Commands.AddAccountCommand;
import org.poo.Commands.AddFundsCommand;
import org.poo.Commands.AddInterestCommand;
import org.poo.Commands.ChangeInterestCommand;
import org.poo.Commands.CheckCardStatusCommand;
import org.poo.Commands.CreateCardCommand;
import org.poo.Commands.CreateOneTimeCardCommand;
import org.poo.Commands.DeleteAccountCommand;
import org.poo.Commands.DeleteCardCommand;
import org.poo.Commands.PayOnlineCommand;
import org.poo.Commands.PrintTransactionsCommand;
import org.poo.Commands.PrintUsersCommand;
import org.poo.Commands.ReportCommand;
import org.poo.Commands.SendMoneyCommand;
import org.poo.Commands.SetAliasCommand;
import org.poo.Commands.SetMinimumBalanceCommand;
import org.poo.Commands.SpendingsReportCommand;
import org.poo.Commands.SplitPaymentCommand;
import org.poo.User.User;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.Map;

public final class BankUtils {
    private BankUtils() {
    }
    /**
     * Processes the sequence of commands and executes them
     * Each command is handled by its respective handler method.
     *
     * @param commands  An array of CommandInput objects representing the commands to be processed.
     * @param output    An ArrayNode used to collect the output of executed commands.
     * @param users     A list of users
     * @param exchanges A list of exchange rates used for currency-related operations.
     */
    public static void transactionFlow(final CommandInput[] commands,
                                       final ArrayNode output, final ArrayList<User> users,
                                       final ArrayList<ExchangeRate> exchanges) {
        for (CommandInput commandInput : commands) {
            Command command = null;
            switch (commandInput.getCommand()) {

                case "addAccount":
                    command = handleAddAccount(commandInput, users);
                    break;

                case "createCard":
                    command = handleCreateCard(commandInput, users);
                    break;

                case "addFunds":
                    command = handleAddFunds(commandInput, users);
                    break;

                case "printUsers":
                    command = new PrintUsersCommand(users, commandInput.getTimestamp());
                    break;

                case "createOneTimeCard":
                    command = handleCreateOneTimeCard(commandInput, users);
                    break;

                case "deleteAccount":
                    command = handleDeleteAccount(commandInput, users);
                    break;

                case "deleteCard":
                    command = handleDeleteCard(commandInput, users);
                    break;

                case "payOnline":
                    command = handlePayOnline(commandInput, users, exchanges);
                    break;

                case "setMinimumBalance":
                    command = handleSetMinimumBalance(commandInput, users);
                    break;

                case "sendMoney":
                    command = handleSendMoney(commandInput, users, exchanges);
                    break;

                case "setAlias":
                    command = handleSetAlias(commandInput, users);
                    break;

                case "printTransactions":
                    command = handlePrintTransactions(commandInput, users);
                    break;

                case "checkCardStatus":
                    command = handleCheckCardStatus(commandInput, users);
                    break;

                case "splitPayment":
                    command = handleSplitPayment(commandInput, users, exchanges);
                    break;

                case "report":
                case "spendingsReport":
                    command = handleReport(commandInput, users);
                    break;

                case "changeInterestRate":
                case "addInterest":
                    command = handleInterestChanges(commandInput, users);
                    break;

                default:

            }
            if (command != null) {
                command.execute(output);
            }
        }
    }

    private static Command handleInterestChanges(final CommandInput commandInput,
                                                 final ArrayList<User> users) {
        for (User currentUser : users) {
            Account account = getAccountByIban(currentUser, commandInput.getAccount());
            if (account != null) {
                if ("changeInterestRate".equals(commandInput.getCommand())) {
                    return new ChangeInterestCommand(currentUser, account,
                            commandInput.getInterestRate(), commandInput.getTimestamp());
                } else {
                    return new AddInterestCommand(account,
                            commandInput.getTimestamp());
                }
            }
        }
        return null;
    }

    private static Command handleReport(final CommandInput commandInput,
                                        final ArrayList<User> users) {
        User accUser = null;
        Account acc = null;

        for (User currentUser : users) {
            acc = getAccountByIban(currentUser, commandInput.getAccount());
            if (acc != null) {
                accUser = currentUser;
                break;
            }
        }

        if (acc != null) {
            if ("report".equals(commandInput.getCommand())) {
                return new ReportCommand(accUser, acc, commandInput.getStartTimestamp(),
                        commandInput.getEndTimestamp(), commandInput.getTimestamp());
            } else {
                return new SpendingsReportCommand(accUser, acc,
                        commandInput.getStartTimestamp(), commandInput.getEndTimestamp(),
                        commandInput.getTimestamp());
            }
        } else {
            if ("report".equals(commandInput.getCommand())) {
                return new ReportCommand(null, null,
                        commandInput.getStartTimestamp(), commandInput.getEndTimestamp(),
                        commandInput.getTimestamp());
            } else {
                return new SpendingsReportCommand(null, null,
                        commandInput.getStartTimestamp(), commandInput.getEndTimestamp(),
                        commandInput.getTimestamp());
            }
        }
    }

    private static Command handleSplitPayment(final CommandInput commandInput,
                                              final ArrayList<User> users,
                                              final ArrayList<ExchangeRate> exchanges) {
        ArrayList<Account> accounts = new ArrayList<>();
        ArrayList<User> usersAssociated = new ArrayList<>();
        int numberAccounts = commandInput.getAccounts().size();
        int currentNumber = 0;
        for (int i = 0; i < numberAccounts; i++) {
            for (User currentUser : users) {
                Account aux = getAccountByIban(currentUser,
                        commandInput.getAccounts().get(i));
                if (aux != null) {
                    accounts.add(aux);
                    usersAssociated.add(currentUser);
                    currentNumber++;
                    break;
                }
            }
        }
        if (currentNumber == numberAccounts) {
            return new SplitPaymentCommand(usersAssociated, accounts,
                    commandInput.getAmount(), commandInput.getCurrency(),
                    commandInput.getTimestamp(), exchanges);
        }
        return null;
    }

    private static Command handleCheckCardStatus(final CommandInput commandInput,
                                                 final ArrayList<User> users) {
        Card card = findCard(commandInput.getCardNumber(), users);
        if (card != null) {
            for (User currentUser : users) {
                Account account = getAccountByIban(currentUser, card.getAssociatedIban());
                if (account != null) {
                    return new CheckCardStatusCommand(currentUser, card, account.getBalance(),
                            account.getMinBalance(), commandInput.getTimestamp());
                }
            }
        } else {
            return new CheckCardStatusCommand(null, null, 0.0, 0.0,
                    commandInput.getTimestamp());
        }
        return null;
    }

    private static Command handlePrintTransactions(final CommandInput commandInput,
                                                   final ArrayList<User> users) {
        User user = getUserByEmail(commandInput.getEmail(), users);
        return new PrintTransactionsCommand(commandInput.getTimestamp(), user);
    }

    private static Command handleSetAlias(final CommandInput commandInput,
                                          final ArrayList<User> users) {
        User user = getUserByEmail(commandInput.getEmail(), users);
        if (user != null) {
            Account account = getAccountByIbanOrAlias(user, commandInput.getAccount());
            return new SetAliasCommand(user, account, commandInput.getAlias(),
                    commandInput.getTimestamp());
        }
        return null;
    }

    private static Command handleSendMoney(final CommandInput commandInput,
                                           final ArrayList<User> users,
                                           final ArrayList<ExchangeRate> exchanges) {
        User senderUser = getUserByEmail(commandInput.getEmail(), users);
        Account accountSender = null;
        Account accountReceiver = null;
        User receiverUser = null;
        if (senderUser != null) {
            accountSender = getAccountByIban(senderUser, commandInput.getAccount());
        }
        for (User currentUser: users) {
            accountReceiver = getAccountByIbanOrAlias(currentUser, commandInput.getReceiver());
            if (accountReceiver != null) {
                receiverUser = currentUser;
                break;
            }
        }
        return new SendMoneyCommand(senderUser, receiverUser, accountSender, accountReceiver,
                commandInput.getAmount(), commandInput.getDescription(), exchanges,
                commandInput.getTimestamp());
    }

    private static Command handleSetMinimumBalance(final CommandInput commandInput,
                                                   final ArrayList<User> users) {
        for (User userCurrent : users) {
            Account account = getAccountByIbanOrAlias(userCurrent, commandInput.getAccount());
            if (account != null) {
                return new SetMinimumBalanceCommand(account, commandInput.getAmount());
            }
        }
        return null;
    }

    private static Command handlePayOnline(final CommandInput commandInput,
                                           final ArrayList<User> users,
                                           final ArrayList<ExchangeRate> exchanges) {
        User user = getUserByEmail(commandInput.getEmail(), users);
        return new PayOnlineCommand(user, commandInput.getCardNumber(),
                commandInput.getAmount(), commandInput.getDescription(),
                commandInput.getCommerciant(), commandInput.getCurrency(),
                commandInput.getTimestamp(), exchanges);
    }

    private static Command handleDeleteCard(final CommandInput commandInput,
                                            final ArrayList<User> users) {
        User user = getUserByEmail(commandInput.getEmail(), users);
        if (user != null) {
            for (Account accountCurrent : user.getAccounts()) {
                for (Card card : accountCurrent.getCards()) {
                    if (card.getCardNumber().equals(commandInput.getCardNumber())) {
                        return new DeleteCardCommand(user, accountCurrent, card,
                                commandInput.getTimestamp());
                    }
                }
            }
        }
        return null;
    }

    private static Command handleAddAccount(final CommandInput commandInput,
                                            final ArrayList<User> users) {
        User user = getUserByEmail(commandInput.getEmail(), users);
        double interestRate = "savings".equals(commandInput.getAccountType())
                ? commandInput.getInterestRate() : 0.0;
        return new AddAccountCommand(user, commandInput.getCurrency(),
                commandInput.getAccountType(), interestRate,
                commandInput.getTimestamp());
    }

    private static Command handleCreateCard(final CommandInput commandInput,
                                            final ArrayList<User> users) {
        User user = getUserByEmail(commandInput.getEmail(), users);
        if (user != null) {
            Account account = getAccountByIbanOrAlias(user, commandInput.getAccount());
            return new CreateCardCommand(account, user, commandInput.getTimestamp());
        }
        return null;
    }

    private static Command handleAddFunds(final CommandInput commandInput,
                                          final ArrayList<User> users) {
        for (User currentUser : users) {
            Account account = getAccountByIbanOrAlias(currentUser, commandInput.getAccount());
            if (account != null) {
                return new AddFundsCommand(account, commandInput.getAmount());
            }
        }
        return null;
    }
    private static Command handleCreateOneTimeCard(final CommandInput commandInput,
                                                   final ArrayList<User> users) {
        User user = getUserByEmail(commandInput.getEmail(), users);
        Account account = null;
        if (user != null) {
            account = getAccountByIbanOrAlias(user, commandInput.getAccount());
        }
        return new CreateOneTimeCardCommand(user, account, commandInput.getTimestamp());

    }
    private static Command handleDeleteAccount(final CommandInput commandInput,
                                               final ArrayList<User> users) {
        User user = getUserByEmail(commandInput.getEmail(), users);
        if (user != null) {
            Account account = getAccountByIbanOrAlias(user, commandInput.getAccount());
            return new DeleteAccountCommand(user, account, commandInput.getTimestamp());
        }
        return null;
    }

    private static Account getAccountByIban(final User user, final String account) {
        for (Account currentAccount: user.getAccounts()) {
            if (currentAccount.getIBAN().equals(account)) {
                return currentAccount;
            }
        }
        return null;
    }

    private static Card findCard(final String cardNumber, final ArrayList<User> users) {
        for (User user: users) {
            for (Account account: user.getAccounts()) {
                for (Card card: account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        return card;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Manages the exchange rates between currencies
     * It constructs an adjacency list representing the exchange rates between each
     * pair of currencies and calculates the rate for each possible currency pair
     *
     * @param exchangesInput an array of ExchangeInput objects containing the initial
     *                       exchange rates
     * @return an ArrayList of ExchangeRate objects representing all the exchange rates
     */
    public static ArrayList<ExchangeRate> manageExchangeRates(final ExchangeInput[]
                                                                      exchangesInput) {
        // Map used to store the adjacency list with a currency as key and as values the rest of
        // the currencies with their respective conversion rates
        Map<String, Map<String, Double>> adjacencyList = new HashMap<>();
        for (ExchangeInput exchangeInput : exchangesInput) {
            adjacencyList.putIfAbsent(exchangeInput.getFrom(), new HashMap<>());
            adjacencyList.putIfAbsent(exchangeInput.getTo(), new HashMap<>());

            adjacencyList.get(exchangeInput.getFrom()).put(exchangeInput.getTo(),
                    exchangeInput.getRate());
            adjacencyList.get(exchangeInput.getTo()).put(exchangeInput.getFrom(),
                    1 / exchangeInput.getRate());
        }

        for (String from : adjacencyList.keySet()) {
            for (String to : adjacencyList.keySet()) {
                if (!from.equals(to) && !adjacencyList.get(from).containsKey(to)) {
                    for (String intermediate : adjacencyList.keySet()) {
                        if (adjacencyList.get(from).containsKey(intermediate) &&
                                adjacencyList.get(intermediate).containsKey(to)) {
                            double newRate = adjacencyList.get(from).get(intermediate) *
                                    adjacencyList.get(intermediate).get(to);

                            adjacencyList.get(from).put(to, newRate);
                            adjacencyList.get(to).put(from, 1 / newRate);
                            break;
                        }
                    }
                }
            }
        }
        ArrayList<ExchangeRate> exchanges = new ArrayList<>();
        for (String from : adjacencyList.keySet()) {
            for (Map.Entry<String, Double> entry : adjacencyList.get(from).entrySet()) {
                if (!from.equals(entry.getKey())) {
                    exchanges.add(new ExchangeRate(from, entry.getKey(), entry.getValue())); //from, to, rate
                }
            }
        }
        return exchanges;
    }

    /**
     * Converts an array of UserInput objects into a list of User objects
     *
     * @param usersInput an array of UserInput objects containing user details
     * @return an ArrayList of User objects
     */
    public static ArrayList<User> manageUsers(final UserInput[] usersInput) {
        ArrayList<User> users = new ArrayList<>();
        for (UserInput userInput : usersInput) {
            users.add(new User(userInput.getEmail(), userInput.getFirstName(),
                    userInput.getLastName()));
        }
        return users;
    }
    private static User getUserByEmail(final String email, final ArrayList<User> users) {
        for (User user: users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
    private static Account getAccountByIbanOrAlias(final User user, final String account) {
        Account acc = user.getAccountAliases().get(account);
        if (acc != null) {
            return acc;
        }
        for (Account currentAccount: user.getAccounts()) {
            if (currentAccount.getIBAN().equals(account)) {
                return currentAccount;
            }
        }
        return null;
    }
}
