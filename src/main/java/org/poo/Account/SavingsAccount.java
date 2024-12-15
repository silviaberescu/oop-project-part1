package org.poo.Account;

public final class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(final String currency,
                          final double interestRate) {
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

    public void setInterestRate(final double interestRate) {
        this.interestRate = interestRate;
    }
}
