package it.polimi.ingsw.server.command;

import it.polimi.ingsw.server.Server;

/**
 * Command to print the number of matches.
 */
@SuppressWarnings("FieldCanBeLocal")
public class numMatchCMD implements ServerCommand{

    private final Server server;
    @SuppressWarnings("unused")
    private final String param;

    /**
     * Construct a Num Match Command with specific parameters.
     * @param param the parameters of the command.
     * @param server the reference to the server instance.
     */
    public numMatchCMD(String param, Server server){
        this.param=param;
        this.server=server;
    }

    /**
     * Print the number of matches.
     */
    @Override
    public void doCommand() {
        if(server.getMatches().size()==0){
            System.out.println("No match on server!");
            return;
        }
        System.out.println("There are "+server.getMatches().size()+" matches!");
    }
}
