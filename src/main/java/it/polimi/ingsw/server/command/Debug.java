package it.polimi.ingsw.server.command;

import it.polimi.ingsw.server.Match;
import it.polimi.ingsw.server.Server;

public class Debug implements ServerCommand{
    private final Server server;
    private final String param;
    public Debug(String param, Server server){
        this.param=param;
        this.server=server;
    }

    @Override
    public void doCommand() {
        if (server.getMatches()!=null){
            server.getMatches().get(0).getController().saveMatchState();
        }
    }
}
