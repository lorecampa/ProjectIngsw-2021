package it.polimi.ingsw.message.serverMessage;

import it.polimi.ingsw.server.ServerMessageHandler;

public class BaseProduction implements ServerMessage{
    @Override
    public void process(ServerMessageHandler handler) {
        handler.handleBaseProduction();

        handler.getVirtualClient().ifPresent(x->x.addToLog(this));
    }
    @Override
    public String toString() {
        return " - Base Production";
    }

}
