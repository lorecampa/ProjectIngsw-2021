package it.polimi.ingsw.message.bothArchitectureMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.server.ServerMessageHandler;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.serverMessage.ServerMessage;

public class PingPongMessage implements ClientMessage, ServerMessage {

    @JsonCreator
    public PingPongMessage() {
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.handlePingPong();
    }

    @Override
    public void process(ServerMessageHandler handler) {}
}
