package it.polimi.ingsw.message.clientMessage;

import it.polimi.ingsw.client.ClientMessageHandler;

//Not a real message, just a clone used to call the main menu in the client
public class MainMenuMessage implements ClientMessage {
    @Override
    public void process(ClientMessageHandler handler) {
        handler.mainMenu();
    }
}
