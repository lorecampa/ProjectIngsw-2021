package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.server.ServerMessageHandler;

public class ReconnectionMessage implements ServerMessage{
    private final int matchID;
    private final int clientID;

    @JsonCreator
    public ReconnectionMessage(@JsonProperty("matchID") int matchID,
                               @JsonProperty("clientID") int clientID) {
        this.matchID = matchID;
        this.clientID = clientID;
    }

    public int getMatchID() {
        return matchID;
    }

    public int getClientID() {
        return clientID;
    }

    @Override
    public void process(ServerMessageHandler handler) {
        handler.handleReconnection(this);
    }
}
