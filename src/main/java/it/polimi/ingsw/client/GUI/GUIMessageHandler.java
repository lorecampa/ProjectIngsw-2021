package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.CLIMessageHandler;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.clientMessage.*;
import javafx.application.Platform;

public class GUIMessageHandler extends ClientMessageHandler {
    private final Client client = Client.getInstance();
    private final ControllerHandler controllerHandler = ControllerHandler.getInstance();



    @Override
    public void handleError(ErrorMessage message) {

    }

    @Override
    public void connectNewUser(ConnectionMessage message) {

    }

    @Override
    public void waitingPeople(ConnectionMessage message) {

    }

    @Override
    public void username(ConnectionMessage message) {

    }

    @Override
    public void numberOfPlayer(ConnectionMessage message) {

    }

    @Override
    public void connectInfo(ConnectionMessage message) {

    }

    @Override
    public void mainMenu() {

    }

    @Override
    public void startGame() {

    }

    @Override
    public void anyConversionRequest(AnyConversionRequest message) {

    }

    @Override
    public void bufferUpdate(BufferUpdate message) {

    }

    @Override
    public void manageResourceRequest(ManageResourcesRequest message) {

    }

    @Override
    public void whiteMarbleConversion(WhiteMarbleConversionRequest message) {

    }

    @Override
    public void winningCondition() {

    }
}
