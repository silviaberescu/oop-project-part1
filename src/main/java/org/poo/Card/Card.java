package org.poo.Card;

import org.poo.utils.Utils;

public abstract class Card {
    private final String cardNumber;
    private String status; // active, frozen, warning, inactive
    private final String associatedIban; //links the card to a specific account
    private static final double WARNING_THRESHOLD = 30;

    public Card(final String associatedIban) {
        this.cardNumber = Utils.generateCardNumber();
        this.associatedIban = associatedIban;
        this.status = "active";
    }

    public final String getCardNumber() {
        return cardNumber;
    }

    public final String getStatus() {
        return status;
    }

    public final String getAssociatedIban() {
        return associatedIban;
    }

    /**
     * Deactivates the card by setting its status to "inactive".
     */
    public final void deactivate() {
        this.status = "inactive";
    }
    /**
     * Sets the card status to "frozen".
     * This indicates that the card has been frozen and cannot be used for transactions
     */
    public final void setFrozen() {
        this.status = "frozen";
    }

    /**
     * Checks if the card status is "frozen".
     * @return true if the card status is "frozen", false otherwise
     */
    public final boolean isFrozen() {
        return status.equals("frozen");

    }

    /**
     * Sets the card status to "warning".
     * This indicates that the card is under a warning status due to a low balance.
     */
    public final void setWarning() {
        this.status = "warning";
    }

    /**
     * Updates the card status based on the balance and minimum balance.
     * If the current balance is less than or equal to the minimum balance,
     * the card status is set to "frozen".
     * If the current balance minus the minimum balance is less than or equal
     * to 30, the card status is set to "warning".
     *
     * @param currentBalance the current balance of the account associated with the card
     * @param minimumBalance the minimum balance threshold for the card
     */
    public void update(final double currentBalance, final double minimumBalance) {
        if (currentBalance <= minimumBalance) {
            setFrozen();
        } else if (currentBalance - minimumBalance <= WARNING_THRESHOLD) {
            setWarning();
        }
    }

    /**
     * Verifies if a payment for the associated card is possible.
     * This method will be implemented by subclasses to handle payment logic
     * according to the specific type of card
     *
     * @return {@code true} if the payment is successfully processed;
     *         {@code false} if the payment cannot be processed.
     */
    public abstract boolean makePayment();
}

