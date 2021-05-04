package it.polimi.ingsw.message.clientMessage;

import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.message.clientMessage.ClientMessage;

public class MainMenuMessage implements ClientMessage {
    @Override
    public void process(ClientMessageHandler handler) {
        handler.mainMenu();
    }
}
