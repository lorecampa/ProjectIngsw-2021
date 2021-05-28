package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.GUI.controller.PersonalBoardController;
import it.polimi.ingsw.client.GUI.controller.PreGameSelectionController;
import it.polimi.ingsw.client.GUI.controller.SetupController;
import it.polimi.ingsw.client.GUI.controller.WaitingController;
import it.polimi.ingsw.client.data.CardLeaderData;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.clientMessage.*;
import javafx.application.Platform;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class GUIMessageHandler extends ClientMessageHandler {
    private final Client client = Client.getInstance();
    private final ControllerHandler controllerHandler = ControllerHandler.getInstance();



    @Override
    public void handleError(ErrorMessage message) {
        String error = (message.getErrorType() == null)?message.getCustomError():message.getErrorType().getMessage();
        Platform.runLater(()->{
            controllerHandler.getCurrentController().showErrorMessage(error);
        });
    }

    @Override
    public void connectNewUser(ConnectionMessage message) {

    }

    @Override
    public void waitingPeople(ConnectionMessage message) {
        WaitingController controller = (WaitingController) controllerHandler.getController(Views.WAITING);
        Platform.runLater(()->{
            controller.setLobbyMessage();
            controllerHandler.changeView(Views.WAITING);
        });
    }

    @Override
    public void username(ConnectionMessage message) {
        SetupController setupController = (SetupController) controllerHandler.getController(Views.SETUP);
        Platform.runLater(()->{
            setupController.showInsertUsername();
            if (controllerHandler.getCurrentView() == Views.MAIN_MENU ||
            controllerHandler.getCurrentView() == Views.WAITING){
                controllerHandler.changeView(Views.SETUP);
            }
        });

    }

    @Override
    public void numberOfPlayer(ConnectionMessage message) {
        Platform.runLater(()->controllerHandler.changeView(Views.SETUP));
    }

    @Override
    public void connectInfo(ConnectionMessage message) {

    }



    @Override
    public void leaderSetUp(LeaderSetUpMessage message) {
        super.leaderSetUp(message);
        PreGameSelectionController controller = (PreGameSelectionController) controllerHandler.getController(Views.PRE_MATCH);
        ArrayList<String> paths = message.getLeaders().stream().map(CardLeaderData::toResourcePath)
                .collect(Collectors.toCollection(ArrayList::new));

        Platform.runLater(()->{
            controllerHandler.changeView(Views.PRE_MATCH);
            controller.setLeaderImages(paths);
        });






    }

    @Override
    public void mainMenu() {
        Platform.runLater(()->controllerHandler.changeView(Views.MAIN_MENU));
    }

    @Override
    public void startGame() {
        Platform.runLater(()->controllerHandler.changeView(Views.PERSONAL_BOARD));
    }


    @Override
    public void anyConversionRequest(AnyConversionRequest message) {
        if(client.getState() == ClientState.ENTERING_LOBBY){
            Platform.runLater(()->{
                if (message.getNumOfAny() == 0){
                    WaitingController controller = (WaitingController) controllerHandler.getController(Views.WAITING);
                    controller.setPreMatchMessage();
                    controllerHandler.changeView(Views.WAITING);
                }else{
                    PreGameSelectionController controller = (PreGameSelectionController) controllerHandler.getController(Views.PRE_MATCH);
                    controller.setHowManyRes(message.getNumOfAny());
                    controller.showChooseResourcesBox();
                }
            });
        }
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
