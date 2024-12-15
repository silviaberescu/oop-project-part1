package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.Account.Account;
import org.poo.Card.Card;
import org.poo.User.User;

public final class ObjectToJsonConverter {

    private ObjectToJsonConverter() {
    }

    /**
     * Creates a JSON node representation of a user. This method constructs an
     * ObjectNode with the user's information. Each account is represented as
     * an ObjectNode as well.
     *
     * @param user  the user object whose data is to be represented in JSON format
     * @return      an ObjectNode representing the user's data
     */
    public static ObjectNode createUserJNode(final User user) {
        ObjectNode userNode = JsonNodeFactory.instance.objectNode();
        userNode.put("firstName", user.getFirstName());
        userNode.put("lastName", user.getLastName());
        userNode.put("email", user.getEmail());
        ArrayNode accountArray = JsonNodeFactory.instance.arrayNode();
        if (!user.getAccounts().isEmpty()) {
            for (Account account: user.getAccounts()) {
                ObjectNode accountNode = createAccountJNode(account);
                accountArray.add(accountNode);
            }
        }
        userNode.set("accounts", accountArray);
        return userNode;
    }

    /**
     * Creates a JSON node representation of an account. This method constructs an
     * ObjectNode with the account's details.
     * Each card is represented as an ObjectNode as well.
     *
     * @param account  the account object whose data is to be represented in JSON format
     * @return        an ObjectNode representing the account's data
     */
    public static ObjectNode createAccountJNode(final Account account) {
        ObjectNode accountNode = JsonNodeFactory.instance.objectNode();
        accountNode.put("IBAN", account.getIBAN());
        accountNode.put("balance", account.getBalance());
        accountNode.put("currency", account.getCurrency());
        accountNode.put("type", account.getType());
        ArrayNode cardArray = JsonNodeFactory.instance.arrayNode();
        if (!account.getCards().isEmpty()) {
            for (Card card: account.getCards()) {
                ObjectNode cardNode = createCardJNode(card);
                cardArray.add(cardNode);
            }
        }
        accountNode.set("cards", cardArray);
        return accountNode;
    }

    /**
     * Creates a JSON node representation of a Card.
     *
     * @param card  the Card object whose data is to be represented in JSON format
     * @return      an ObjectNode representing the card's data
     */
    public static ObjectNode createCardJNode(final Card card) {
        ObjectNode cardNode = JsonNodeFactory.instance.objectNode();
        cardNode.put("cardNumber", card.getCardNumber());
        cardNode.put("status", card.getStatus());
        return cardNode;
    }

    /**
     * Creates a JSON node indicating that the specified card was not found.
     *
     * @param command   the name of the command that triggered the error
     * @param timestamp the timestamp when the error occurred
     * @return          an ObjectNode representing the output
     */
    public static ObjectNode cardNotFoundJsonNode(final String command,
                                                  final int timestamp) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", command);
        ObjectNode outputNode = JsonNodeFactory.instance.objectNode();
        outputNode.put("timestamp", timestamp);
        outputNode.put("description", "Card not found");
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        return node;
    }

    /**
     * Creates a JSON node indicating an error related to accessing a non-savings account,
     * when it was not supposed to. This method generates a JSON structure containing an
     * error message when an operation is attempted on a non-savings account.
     *
     * @param command   the name of the command that triggered the error
     * @param timestamp the timestamp when the error occurred
     * @param exception the error message
     * @return          an ObjectNode representing the output
     */
    public static ObjectNode notSavingsAccJsonNode(final String command,
                                                    final int timestamp,
                                                    final String exception) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", command);
        ObjectNode outputNode = JsonNodeFactory.instance.objectNode();
        outputNode.put("timestamp", timestamp);
        outputNode.put("description", exception);
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        return node;
    }
}
