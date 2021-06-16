package it.polimi.ingsw.client.command;

public interface Command {

    /**
     * Execute the command
     * */
    void doCommand();
    /**
     * Print the help of current command, all the info u need to know about the command
     * */
    void help();

    /**
     * Print the description of current command
     * */
    void description();
}
