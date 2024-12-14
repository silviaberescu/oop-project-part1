package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.Account.Account;
import org.poo.Card.Card;
import org.poo.User.User;

import java.util.ArrayList;
import java.util.List;

public interface Command {
    void execute(ArrayNode output);
}


