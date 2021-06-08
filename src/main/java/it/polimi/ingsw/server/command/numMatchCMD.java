package it.polimi.ingsw.server.command;

import it.polimi.ingsw.server.Server;

public class numMatchCMD implements ServerCommand{

    private final Server server;
    private final String param;
    public numMatchCMD(String param, Server server){
        this.param=param;
        this.server=server;
    }

    @Override
    public void doCommand() {
        if(server.getMatches().size()==0){
            System.out.println("No match on server!");
            return;
        }
        System.out.println("There are "+server.getMatches().size()+" matches!");

    }
}
