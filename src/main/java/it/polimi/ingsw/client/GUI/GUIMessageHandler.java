package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.clientMessage.*;
import javafx.application.Platform;

public class GUIMessageHandler extends ClientMessageHandler {

    public GUIMessageHandler(Client client) {
        super(client);
    }
}
