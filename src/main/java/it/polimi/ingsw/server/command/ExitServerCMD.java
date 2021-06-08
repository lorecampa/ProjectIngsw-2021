package it.polimi.ingsw.server.command;

import it.polimi.ingsw.server.Server;

public class ExitServerCMD implements ServerCommand{

    private final Server server;
    private final String param;
    public ExitServerCMD(String param, Server server){
        this.param=param;
        this.server=server;
    }

    @Override
    public void doCommand() {
        System.out.println("Hope you enjoyed MastersOfRenaissance! See you soon!");
        System.exit(0);
    }
}
