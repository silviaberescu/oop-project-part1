package org.poo.Transactions;

public class TransactionFactory {
    public enum TransactionType {
        GENERIC, TRANSFER, SPLIT_PAYMENT, SPLIT_PAYMENT_ERROR, NEW_CARD, DELETE_CARD, CARD_PAYMENT
    }
    /**
     * Creates a Transaction based on the specified TransactionType and TransactionData.
     *
     * @param type the type of transaction to create
     * @param data the data required to initialize the transaction.
     * @return a Transaction instance or one of its subclasses based on the type
     *
     */
    public static Transaction createTransaction(final TransactionType type,
                                                final TransactionData data) {
        return switch (type) {
            case GENERIC -> new Transaction(
                    data.getTimestamp(),
                    data.getDescription(),
                    data.getAssociatedIban()
            );
            case TRANSFER -> new TransferTransaction(
                    data.getTimestamp(),
                    data.getDescription(),
                    data.getAssociatedIban(),
                    data.getSenderIban(),
                    data.isSent(),
                    data.getAmount(),
                    data.getReceiverIban(),
                    data.getCurrency()
            );
            case SPLIT_PAYMENT -> new SplitPaymentTransaction(
                    data.getTimestamp(),
                    data.getDescription(),
                    data.getAssociatedIban(),
                    data.getCurrency(),
                    data.getAmount(),
                    data.getAccounts()
            );
            case SPLIT_PAYMENT_ERROR -> new SplitPaymentErrorTransaction(
                    data.getTimestamp(),
                    data.getDescription(),
                    data.getAssociatedIban(),
                    data.getCurrency(),
                    data.getAmount(),
                    data.getAccounts(),
                    data.getError()
            );
            case NEW_CARD -> new NewCardTransaction(
                    data.getTimestamp(),
                    data.getDescription(),
                    data.getCardNumber(),
                    data.getEmail(),
                    data.getAssociatedIban()
            );
            case DELETE_CARD -> new DeleteCardTransaction(
                    data.getTimestamp(),
                    data.getDescription(),
                    data.getCardNumber(),
                    data.getEmail(),
                    data.getAssociatedIban()
            );
            case CARD_PAYMENT -> new CardPaymentTransaction(
                    data.getAssociatedIban(),
                    data.getTimestamp(),
                    data.getDescription(),
                    data.getAmount(),
                    data.getCommerciant()
            );
        };
    }
}
