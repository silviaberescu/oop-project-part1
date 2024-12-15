package org.poo.Bank;

public final class ExchangeRate {
    private String from;
    private String to;
    private double rate;

    public ExchangeRate(final String from, final String to,
                        final double rate) {
        this.from = from;
        this.rate = rate;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(final String from) {
        this.from = from;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(final int rate) {
        this.rate = rate;
    }

    public String getTo() {
        return to;
    }

    public void setTo(final String to) {
        this.to = to;
    }
}
