package org.poo.Account;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String currency, double interestRate) {
        super(currency);
        this.interestRate = interestRate;
    }

    @Override
    public String getType() {
        return "savings";
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}
