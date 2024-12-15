package org.poo.Commands;

import com.fasterxml.jackson.databind.node.ArrayNode;

public interface Command {
    /**
     * Executes the command and populates the provided ArrayNode with the result.
     *
     * This method performs the command's logic and adds the outcome to the provided
     * ArrayNode in JSON format.
     *
     * @param output the ArrayNode to be populated with the command's result
     */
    void execute(ArrayNode output);
}


