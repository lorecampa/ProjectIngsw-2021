package it.polimi.ingsw.server.command;

import it.polimi.ingsw.server.Match;
import it.polimi.ingsw.server.Server;

public class ListMatchCMD implements ServerCommand{
    private final Server server;
    private final String param;
    public ListMatchCMD(String param, Server server){
        this.param=param;
        this.server=server;
    }

    @Override
    public void doCommand() {
        if(server.getMatches().size()==0){
            System.out.println("No match on server!");
            return;
        }
        for(Match match : server.getMatches()){
            System.out.println("Id match: "+match.getMatchID()+", with "+ match.getNumOfPlayers()+" players. Connected: "+ match.currentNumOfPlayer());
        }

    }
}
