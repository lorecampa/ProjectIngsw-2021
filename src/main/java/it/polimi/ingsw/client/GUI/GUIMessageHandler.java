package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.GUI.controller.*;
import it.polimi.ingsw.client.data.CardLeaderData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.clientMessage.*;
import it.polimi.ingsw.model.resource.ResourceType;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class GUIMessageHandler extends ClientMessageHandler {
    private final Client client = Client.getInstance();
    private final ControllerHandler controllerHandler = ControllerHandler.getInstance();


    @Override
    public void reconnect(ReconnectionMessage message){
        try {
            super.reconnect(message);
            Platform.runLater(()->{
                controllerHandler.getCurrentController().showCustomMessage("We saved your main data, making sure you can disconnect and reconnect during the game!");
            });
        } catch (IOException e) {
            Platform.runLater(()->{
                controllerHandler.getCurrentController().showCustomMessage("Not able to save the file with your info to reconnect!");
            });
        }
    }

    @Override
    public void reconnectGameSetUp(ReconnectGameMessage message) {
        super.reconnectGameSetUp(message);
        Platform.runLater(()->{
            controllerHandler.getCurrentController().showCustomMessage("It's your turn!");
        });
    }

    @Override
    public void validReconnect(ConnectionMessage message) {
        super.validReconnect(message);
        Platform.runLater(()->{
            controllerHandler.getCurrentController().showCustomMessage("Reconnected as "+message.getMessage()+"!");
        });
    }

    @Override
    public void strongboxUpdate(StrongboxUpdate message) {
        super.strongboxUpdate(message);
        Platform.runLater(()->{
            PersonalBoardController personalBoardController = (PersonalBoardController) ControllerHandler.getInstance().getController(Views.PERSONAL_BOARD);
            if(message.getUsername().equals(personalBoardController.getCurrentShowed())) {
                personalBoardController.loadStrongBox(Client.getInstance().getModelOf(message.getUsername()).toModelData());
            }
        });
    }

    @Override
    public void handleError(ErrorMessage message) {
        String error = (message.getErrorType() == null)?message.getCustomError():message.getErrorType().getMessage();
        Platform.runLater(()->{
            controllerHandler.getCurrentController().showCustomMessage(error);
        });
    }

    @Override
    public void connectNewUser(ConnectionMessage message) {
        Platform.runLater(()->{
            controllerHandler.getCurrentController().showCustomMessage(message.getMessage());
        });
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
    public void newTurn(StarTurn message) {
        super.newTurn(message);
        String msg = "Is "+ message.getUsername() + " turn";
        Platform.runLater(()->{
            controllerHandler.getCurrentController().showCustomMessage(msg);
        });
    }

    @Override
    public void numberOfPlayer(ConnectionMessage message) {
        Platform.runLater(()->controllerHandler.changeView(Views.SETUP));
    }

    @Override
    public void connectInfo(ConnectionMessage message) {
        Platform.runLater(()->{
            controllerHandler.getCurrentController().showCustomMessage(message.getMessage());
        });
    }



    @Override
    public void leaderSetUp(LeaderSetUpMessage message) {
        super.leaderSetUp(message);
        PreGameSelectionController controller = (PreGameSelectionController) controllerHandler.getController(Views.PRE_MATCH);

        Platform.runLater(()->{
            controllerHandler.changeView(Views.PRE_MATCH);
            controller.setUpLeader(message.getLeaders());
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
    public void gameSetUp(GameSetup message) {
        super.gameSetUp(message);

        ArrayList<ResourceData> resources = new ArrayList<>();
        resources.add(new ResourceData(ResourceType.COIN, 3));
        resources.add(new ResourceData(ResourceType.SERVANT, 3));
        resources.add(new ResourceData(ResourceType.STONE, 4));
        //resources.add(new ResourceData(ResourceType.SHIELD, 5));

        Platform.runLater(()->{
            PersonalBoardController pbController = (PersonalBoardController) controllerHandler.getController(Views.PERSONAL_BOARD);
            pbController.updateDepot(0, new ResourceData(ResourceType.COIN, 1), true);
            pbController.updateDepot(2, new ResourceData(ResourceType.SHIELD, 2), true);

        });

        /*
        Platform.runLater(()->{
            PersonalBoardController pbController = (PersonalBoardController) controllerHandler.getController(Views.PERSONAL_BOARD);
            pbController.setUpResourceFromMarket(resources);
        });

         */

        Platform.runLater(()->{
            DeckDevelopmentController deckController = (DeckDevelopmentController) controllerHandler.getController(Views.DECK_DEV);
            deckController.setUpDeckImages(message.getDeckDev());
        });
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
    public void leaderDiscardUpdate(LeaderDiscard message) {
        super.leaderDiscardUpdate(message);
        Platform.runLater(()->{
            PersonalBoardController personalBoardController = (PersonalBoardController) ControllerHandler.getInstance().getController(Views.PERSONAL_BOARD);
            if(message.getUsername().equals(personalBoardController.getCurrentShowed())) {
                personalBoardController.resetLeader();
                personalBoardController.loadLeader(Client.getInstance().getModelOf(message.getUsername()).toModelData());
            }
        });
    }

    @Override
    public void bufferUpdate(BufferUpdate message) {

    }

    @Override
    public void manageResourceRequest(ManageResourcesRequest message) {
        Platform.runLater(()->{
            PersonalBoardController pbController = (PersonalBoardController) controllerHandler.getController(Views.PERSONAL_BOARD);
            pbController.setUpResourceFromMarket(message.getResources());
        });
    }

    @Override
    public void whiteMarbleConversion(WhiteMarbleConversionRequest message) {

    }

    @Override
    public void faithTrackPositionIncreased(FaithTrackIncrement message) {
        super.faithTrackPositionIncreased(message);
        Platform.runLater(()->{
            PersonalBoardController personalBoardController = (PersonalBoardController) ControllerHandler.getInstance().getController(Views.PERSONAL_BOARD);
            if(message.getUsername().equals(personalBoardController.getCurrentShowed())) {
                personalBoardController.resetFaithTrack();
                personalBoardController.loadFaithTrack(Client.getInstance().getModelOf(message.getUsername()).toModelData());
            }
        });
    }

    @Override
    public void popeFavorActivation(PopeFavorActivated message) {
        super.popeFavorActivation(message);
        Platform.runLater(()->{
            PersonalBoardController personalBoardController = (PersonalBoardController) ControllerHandler.getInstance().getController(Views.PERSONAL_BOARD);
            if(message.getUsername().equals(personalBoardController.getCurrentShowed())) {
                personalBoardController.resetFaithTrack();
                personalBoardController.loadFaithTrack(Client.getInstance().getModelOf(message.getUsername()).toModelData());
            }
        });
    }

    @Override
    public void activeLeader(LeaderActivate message) {
        super.activeLeader(message);
        Platform.runLater(()->{
            PersonalBoardController personalBoardController = (PersonalBoardController) ControllerHandler.getInstance().getController(Views.PERSONAL_BOARD);
            if(message.getUsername().equals(personalBoardController.getCurrentShowed())) {
                personalBoardController.resetLeader();
                personalBoardController.loadLeader(Client.getInstance().getModelOf(message.getUsername()).toModelData());
            }
        });
    }

    @Override
    public void cardSlotUpdate(CardSlotUpdate message) {
        super.cardSlotUpdate(message);
        Platform.runLater(()->{
            PersonalBoardController personalBoardController = (PersonalBoardController) ControllerHandler.getInstance().getController(Views.PERSONAL_BOARD);
            if(message.getUsername().equals(personalBoardController.getCurrentShowed())) {
                personalBoardController.resetCardSlots();
                personalBoardController.loadCardSlots(Client.getInstance().getModelOf(message.getUsername()).toModelData());
            }
        });
    }

    @Override
    public void depotUpdate(DepotUpdate message) {
        super.depotUpdate(message);
        Platform.runLater(()->{
            PersonalBoardController personalBoardController = (PersonalBoardController) ControllerHandler.getInstance().getController(Views.PERSONAL_BOARD);
            if(message.getUsername().equals(personalBoardController.getCurrentShowed())) {
                personalBoardController.resetStandardDepots();
                personalBoardController.loadStandardDepots(Client.getInstance().getModelOf(message.getUsername()).toModelData());
            }
        });
    }

    @Override
    public void depotLeaderUpdate(DepotLeaderUpdate message) {
        super.depotLeaderUpdate(message);
        Platform.runLater(()->{
            PersonalBoardController personalBoardController = (PersonalBoardController) ControllerHandler.getInstance().getController(Views.PERSONAL_BOARD);
            if(message.getUsername().equals(personalBoardController.getCurrentShowed())) {
                personalBoardController.resetLeaderDepots();
                personalBoardController.loadLeaderDepots(Client.getInstance().getModelOf(message.getUsername()).toModelData());
            }
        });
    }



    @Override
    public void winningCondition() {

    }


}
