package it.polimi.ingsw.message;

import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.server.ServerMessageHandler;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.serverMessage.ServerMessage;

public class PingPongMessage implements ClientMessage, ServerMessage {
    @Override
    public void process(ClientMessageHandler handler) {

    }

    @Override
    public void process(ServerMessageHandler handler) {

    }
}
