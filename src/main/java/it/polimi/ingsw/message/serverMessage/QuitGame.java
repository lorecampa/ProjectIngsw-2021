package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.server.ServerMessageHandler;

public class QuitGame implements ServerMessage{
    @Override
    public void process(ServerMessageHandler handler) {
        handler.handleDisconnection();
    }
}
