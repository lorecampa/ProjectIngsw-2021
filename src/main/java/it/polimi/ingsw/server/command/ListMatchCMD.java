package it.polimi.ingsw.server.command;

import it.polimi.ingsw.server.Match;
import it.polimi.ingsw.server.Server;

/**
 * Command to print the current matches.
 */
@SuppressWarnings("FieldCanBeLocal")
public class ListMatchCMD implements ServerCommand{
    private final Server server;
    @SuppressWarnings("unused")
    private final String param;


    /**
     * Construct a List Match Command with specific parameters.
     * @param param the parameters of the command.
     * @param server the reference to the server instance.
     */
    public ListMatchCMD(String param, Server server){
        this.param=param;
        this.server=server;
    }

    /**
     * Print the list of current matches.
     */
    @Override
    public void doCommand() {
        if(server.getMatches().size()==0){
            System.out.println("No match on server!");
            return;
        }
        for(Match match : server.getMatches()){
            System.out.println("Id match: "+match.getMatchID()+", with "+ match.getNumOfPlayers()+" players. Connected: "+ match.getActivePlayers().size());
        }
    }
}
