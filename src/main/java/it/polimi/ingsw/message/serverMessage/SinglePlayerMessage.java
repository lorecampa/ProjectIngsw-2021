package it.polimi.ingsw.message.serverMessage;

import it.polimi.ingsw.server.ServerMessageHandler;

public class SinglePlayerMessage implements ServerMessage{

    @Override
    public void process(ServerMessageHandler handler) {
        handler.handleSinglePlayer();

        handler.getVirtualClient().ifPresent(x->x.addToLog(this));
    }

    @Override
    public String toString() {
        return " - Single Player Message";
    }
}
