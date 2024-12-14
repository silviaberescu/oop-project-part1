package org.poo.Bank;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Account.Account;
import org.poo.Card.Card;
import org.poo.Commands.*;
import org.poo.User.User;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;

public class BankUtils {
    public BankUtils() {
    }
    public static void transactionFlow(final CommandInput[] commands, final ArrayNode output, ArrayList<User> users,
                                       ArrayList<ExchangeRate> exchanges) {
        for(CommandInput commandInput : commands) {
            Command command = null;
            Account account = null;
            User user;
            switch (commandInput.getCommand()) {

                case "addAccount":
                    user = getUserByEmail(commandInput.getEmail(), users);
                    double interestRate = commandInput.getAccountType().equals("savings") ?
                            commandInput.getInterestRate() : 0.0;
                    command = new AddAccountCommand(user, commandInput.getCurrency(), commandInput.getAccountType(),
                            interestRate, commandInput.getTimestamp());
                    break;

                case "createCard":
                    user = getUserByEmail(commandInput.getEmail(), users);
                    if (user != null) {
                        account = getAccountByIbanOrAlias(user, commandInput.getAccount());
                    }
                    command = new CreateCardCommand(account, user, commandInput.getTimestamp());
                    break;

                case "addFunds":
                    for(User currentUser: users) {
                        account = getAccountByIbanOrAlias(currentUser, commandInput.getAccount());
                        if(account != null) {
                            command = new AddFundsCommand(account, commandInput.getAmount());
                            break;
                        }
                    }
                    break;

                case "printUsers":
                    command = new PrintUsersCommand(users, commandInput.getTimestamp());
                    break;

                case "createOneTimeCard":
                    user = getUserByEmail(commandInput.getEmail(), users);
                    if (user != null) {
                        account = getAccountByIbanOrAlias(user, commandInput.getAccount());
                    }
                    if(account != null){
                        command = new CreateOneTimeCardCommand(user, account, commandInput.getTimestamp());
                    }
                    break;

                case "deleteAccount":
                    user = getUserByEmail(commandInput.getEmail(), users);
                    if (user != null) {
                        account = getAccountByIbanOrAlias(user, commandInput.getAccount());
                    }
                    command = new DeleteAccountCommand(user, account, commandInput.getTimestamp());
                    break;

                case "deleteCard":
                    user = getUserByEmail(commandInput.getEmail(), users);
                    if (user != null) {
                        for (Account accountCurrent: user.getAccounts()) {
                            for (Card card: accountCurrent.getCards()) {
                                if (card.getCardNumber().equals(commandInput.getCardNumber())) {
                                    command = new DeleteCardCommand(user, accountCurrent,
                                            card, commandInput.getTimestamp());
                                    break;
                                }
                            }
                        }
                    }
                    break;

                case "payOnline":
                    user = getUserByEmail(commandInput.getEmail(), users);
                    command = new PayOnlineCommand(user, commandInput.getCardNumber(), commandInput.getAmount(),
                            commandInput.getDescription(), commandInput.getCommerciant(), commandInput.getCurrency(),
                            commandInput.getTimestamp(), exchanges);
                    break;

                case "setMinimumBalance":
                    for(User userCurrent: users){
                        account = getAccountByIbanOrAlias(userCurrent, commandInput.getAccount());
                        if(account != null){
                            command =  new SetMinimumBalanceCommand(account, commandInput.getAmount(),
                                    commandInput.getTimestamp());
                        }
                    }
                    break;

                case "sendMoney":
                    User senderUser = getUserByEmail(commandInput.getEmail(), users);
                    Account accountSender = null;
                    Account accountReceiver = null;
                    User receiverUser = null;
                    if (senderUser != null) {
                        accountSender = getAccountByIban(senderUser, commandInput.getAccount());
                    }
                    for(User currentUser: users) {
                        accountReceiver = getAccountByIbanOrAlias(currentUser, commandInput.getReceiver());
                        if(accountReceiver != null) {
                            receiverUser = currentUser;
                            break;
                        }
                    }
                    command = new SendMoneyCommand(senderUser, receiverUser, accountSender, accountReceiver, commandInput.getAmount(),
                            commandInput.getDescription(), exchanges, commandInput.getTimestamp());
                    break;

                case "setAlias":
                    user = getUserByEmail(commandInput.getEmail(), users);
                    account = getAccountByIbanOrAlias(user, commandInput.getAccount());
                    command = new SetAliasCommand(user, account, commandInput.getAlias(), commandInput.getTimestamp());
                    break;

                case "printTransactions":
                    user = getUserByEmail(commandInput.getEmail(), users);
                    command = new PrintTransactionsCommand(commandInput.getTimestamp(), user);
                    break;

                case "checkCardStatus":
                    Card card = findCard(commandInput.getCardNumber(), users);
                    if (card != null){
                        for (User currentUser: users) {
                            account = getAccountByIban(currentUser, card.getAssociatedIban());
                            if(account != null) {
                                command = new CheckCardStatusCommand(currentUser, card, account.getBalance(),
                                        account.getMinBalance(), commandInput.getTimestamp());
                            }
                        }
                    } else {
                        command = new CheckCardStatusCommand(null, null, 0.0,
                                0.0, commandInput.getTimestamp());
                    }
                    break;

                case "splitPayment":
                    ArrayList<Account> accounts = new ArrayList<>();
                    ArrayList<User> usersAssociated = new ArrayList<>();
                    int numberAccounts = commandInput.getAccounts().size();
                    int currentNumber = 0;
                    for(int i = 0; i < numberAccounts; i++) {
                        for (User currentUser: users) {
                            Account aux = getAccountByIban(currentUser, commandInput.getAccounts().get(i));
                            if(aux != null){
                                accounts.add(aux);
                                usersAssociated.add(currentUser);
                                currentNumber++;
                                break;
                            }
                        }
                    }
                    if(currentNumber == numberAccounts){
                        command = new SplitPaymentCommand (usersAssociated, accounts, commandInput.getAmount(),
                                commandInput.getCurrency(), commandInput.getTimestamp(), exchanges);
                    }
                    break;

                case "report":
                case "spendingsReport":
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
                            command = new ReportCommand(accUser, acc, commandInput.getStartTimestamp(),
                                    commandInput.getEndTimestamp(), commandInput.getTimestamp());
                        } else {
                            command = new SpendingsReportCommand(accUser, acc, commandInput.getStartTimestamp(),
                                    commandInput.getEndTimestamp(), commandInput.getTimestamp());
                        }
                    } else {
                        if ("report".equals(commandInput.getCommand())) {
                            command = new ReportCommand(null, null, commandInput.getStartTimestamp(),
                                    commandInput.getEndTimestamp(), commandInput.getTimestamp());
                        } else {
                            command = new SpendingsReportCommand(null, null, commandInput.getStartTimestamp(),
                                    commandInput.getEndTimestamp(), commandInput.getTimestamp());
                        }
                    }
                    break;

                case "changeInterestRate":
                case "addInterest":
                    for (User currentUser : users) {
                        account = getAccountByIban(currentUser, commandInput.getAccount());
                        if (account != null) {
                            if ("changeInterestRate".equals(commandInput.getCommand())) {
                                command = new ChangeInterestCommand(currentUser, account, commandInput.getInterestRate(),
                                        commandInput.getTimestamp());
                            } else {
                                command = new AddInterestCommand(currentUser, account, commandInput.getTimestamp());
                            }
                            break;
                        }
                    }
                    break;

                default:

            }
            if (command != null) {
                command.execute(output);
            }
        }
    }
    private static Account getAccountByIban(User user, String account) {
        for(Account currentAccount: user.getAccounts()){
            if(currentAccount.getIBAN().equals(account)){
                return currentAccount;
            }
        }
        return null;
    }
    private static Card findCard(String cardNumber, ArrayList<User> users){
        for (User user: users) {
            for(Account account: user.getAccounts()) {
                for(Card card: account.getCards()) {
                    if(card.getCardNumber().equals(cardNumber)) {
                        return card;
                    }
                }
            }
        }
        return null;
    }

    public static ArrayList<ExchangeRate> manageExchangeRates(ExchangeInput[] exchangesInput) {
        if (exchangesInput.length == 0) return null;

        ArrayList<String> currencyList = new ArrayList<>();
        for (ExchangeInput exchangeInput : exchangesInput) {
            if (!currencyList.contains(exchangeInput.getFrom())) {
                currencyList.add(exchangeInput.getFrom());
            }
            if (!currencyList.contains(exchangeInput.getTo())) {
                currencyList.add(exchangeInput.getTo());
            }
        }

        int n = currencyList.size();

        double[][] rates = new double[n][n];
        for (int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                rates[i][j] = (i == j) ? 1.0 : Double.POSITIVE_INFINITY;
            }
        }

        for (ExchangeInput exchangeInput : exchangesInput) {
            int fromIdx = currencyList.indexOf(exchangeInput.getFrom());
            int toIdx = currencyList.indexOf(exchangeInput.getTo());
            rates[fromIdx][toIdx] = exchangeInput.getRate();
            rates[toIdx][fromIdx] = 1 / exchangeInput.getRate();
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (rates[i][k] < Double.POSITIVE_INFINITY && rates[k][j] < Double.POSITIVE_INFINITY) {
                        rates[i][j] = Math.min(rates[i][j], rates[i][k] * rates[k][j]);
                    }
                }
            }
        }

        ArrayList<ExchangeRate> exchanges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && rates[i][j] < Double.POSITIVE_INFINITY) {
                    exchanges.add(new ExchangeRate(currencyList.get(i), currencyList.get(j), rates[i][j]));
                }
            }
        }
        return exchanges;
    }


    public static ArrayList<User> manageUsers(UserInput[] usersInput){
        ArrayList<User> users = new ArrayList<>();
        for(UserInput userInput : usersInput) {
            users.add(new User(userInput.getEmail(), userInput.getFirstName(), userInput.getLastName()));
        }
        return users;
    }
    private static User getUserByEmail(String email, ArrayList<User> users){
        for(User user: users){
            if(user.getEmail().equals(email))
                return user;
        }
        return null;
    }
    private static Account getAccountByIbanOrAlias(User user, String account){
        Account acc = user.getAccountAliases().get(account);
        if(acc != null){
            return acc;
        }
        for(Account currentAccount: user.getAccounts()){
            if(currentAccount.getIBAN().equals(account)){
                return currentAccount;
            }
        }
        return null;
    }
}
