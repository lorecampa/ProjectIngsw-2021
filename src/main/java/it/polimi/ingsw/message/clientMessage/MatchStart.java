package it.polimi.ingsw.message.clientMessage;

import it.polimi.ingsw.client.ClientMessageHandler;

public class MatchStart implements ClientMessage{

    @Override
    public void process(ClientMessageHandler handler) {
        handler.startGame();
    }
}
