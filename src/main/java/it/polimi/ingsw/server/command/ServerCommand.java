package it.polimi.ingsw.server.command;

/**
 * A class implement ServerCommand when it can perform a user input command.
 */
public interface ServerCommand {

    /**
     * Perform the command based on the class.
     */
    void doCommand();
}
