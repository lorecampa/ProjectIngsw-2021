package it.polimi.ingsw.message.serverMessage;

import it.polimi.ingsw.server.ServerMessageHandler;

public class QuitGame implements ServerMessage{
    @Override
    public void process(ServerMessageHandler handler) {
        handler.handleDisconnection();
        handler.getVirtualClient().ifPresent(x->x.addToLog(this));
    }

    @Override
    public String toString() {
        return " - Quit Game";
    }
}
