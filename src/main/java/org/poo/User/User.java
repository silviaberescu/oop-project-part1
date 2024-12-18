package org.poo.User;

import org.poo.Account.Account;
import org.poo.Transactions.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class User {
    private final String email;
    private final String firstName;
    private final String lastName;
    //A list of the user's accounts
    private final List<Account> accounts;
    //A history of all transactions associated with the user
    private final List<Transaction> transactions;
    private final Map<String, Account> accountAliases;


    public User(final String email, final String firstName,
                final String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.accountAliases = new HashMap<>();
    }
    /**
     * Adds a new transaction to the list of transactions
     *
     * @param newTransaction the Transaction object to be added
     */
    public void addTransaction(final Transaction newTransaction) {
        this.transactions.add(newTransaction);
    }
    /**
     * Adds a new account to the list of accounts
     *
     * @param newAccount the Account object to be added to the list
     */
    public void addAccount(final Account newAccount) {
        this.accounts.add(newAccount);
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Map<String, Account> getAccountAliases() {
        return accountAliases;
    }

    /**
     * Associates an alias with a given account
     *
     * @param alias the alias to be associated with the account
     * @param account the Account object to which the alias will be assigned
     */
    public void addAlias(final String alias, final Account account) {
        accountAliases.put(alias, account);
        account.setAlias(alias);
    }
}
