package it.polimi.ingsw.server.command;

import it.polimi.ingsw.server.Server;

/**
 * Command to close the server.
 */
@SuppressWarnings("FieldCanBeLocal")
public class ExitServerCMD implements ServerCommand{
    @SuppressWarnings("unused")
    private final Server server;
    @SuppressWarnings("unused")
    private final String param;

    /**
     * Construct a Exit Server Command with specific parameters.
     * @param param the parameters of the command.
     * @param server the reference to the server instance.
     */
    public ExitServerCMD(String param, Server server){
        this.param=param;
        this.server=server;
    }

    /**
     * Close the server.
     */
    @Override
    public void doCommand() {
        server.setExit(true);
    }
}
