package org.poo.User;

import org.poo.Account.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private final String email;
    private final String firstName;
    private final String lastName;
    private List<Account> accounts;
    private List<Transaction> transactions;
    private final Map<String, Account> accountAliases;


    public User(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.accountAliases = new HashMap<>();
    }

    public void addTransaction(Transaction newTransaction){
        this.transactions.add(newTransaction);
    }

    public void addAccount(Account newAccount) {
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

    public void addAlias(String alias, Account account) {
        accountAliases.put(alias, account);
        account.setAlias(alias);
    }
}
