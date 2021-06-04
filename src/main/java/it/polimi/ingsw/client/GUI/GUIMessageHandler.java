package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.GUI.controller.*;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.clientMessage.*;
import javafx.application.Platform;

import java.io.IOException;


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
        Platform.runLater(()->{
            WaitingController controller = (WaitingController) controllerHandler.getController(Views.WAITING);
            controller.setLobbyMessage();
            controllerHandler.changeView(Views.WAITING);
        });
    }

    @Override
    public void username(ConnectionMessage message) {
        Platform.runLater(()->{
            SetupController setupController = (SetupController) controllerHandler.getController(Views.SETUP);
            if (controllerHandler.getCurrentView() == Views.MAIN_MENU || controllerHandler.getCurrentView() == Views.WAITING){
                controllerHandler.changeView(Views.SETUP);
            }
            setupController.showInsertUsername();
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
        Platform.runLater(()->{
            controllerHandler.changeView(Views.SETUP);
        });
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
        Platform.runLater(()->{
            PreGameSelectionController controller = (PreGameSelectionController) controllerHandler.getController(Views.PRE_MATCH);
            controllerHandler.changeView(Views.PRE_MATCH);
            controller.setUpLeaderImages(message.getLeaders());
        });
    }



    @Override
    public void mainMenu() {
        Platform.runLater(()->controllerHandler.changeView(Views.MAIN_MENU));
    }

    @Override
    public void startGame() {
        Platform.runLater(()->{
            controllerHandler.stopMusic();
            controllerHandler.changeView(Views.PERSONAL_BOARD);
        });
    }

    @Override
    public void gameSetUp(GameSetup message) {
        super.gameSetUp(message);
    }

    @Override
    public void gameOver(GameOver message) {
        super.gameOver(message);
        Platform.runLater(()->{
            GameEndedController controller = (GameEndedController) controllerHandler.getController(Views.GAME_END);
            controller.setUpRanking(message.getPlayers());
            ControllerHandler.getInstance().changeView(Views.GAME_END);
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
        }else{
            Platform.runLater(()->{
                PersonalBoardController controller = (PersonalBoardController) controllerHandler.getController(Views.PERSONAL_BOARD);
                controller.setBufferLabel("You have to convert " + message.getNumOfAny() + " into concrete resources");
                controller.setUpAnyConversion(message.getOptionConversion(), message.getNumOfAny());
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
                personalBoardController.resetLeaderDepots();
                personalBoardController.loadLeader(Client.getInstance().getModelOf(message.getUsername()).toModelData());
                personalBoardController.loadLeaderDepots(Client.getInstance().getModelOf(message.getUsername()).toModelData());
            }
        });

    }


    @Override
    public void marketUpdate(MarketUpdate message) {
        super.marketUpdate(message);
        MarketController controller = (MarketController) ControllerHandler.getInstance().getController(Views.MARKET);
        Platform.runLater(controller::setUpAll);
    }



    @Override
    public void bufferUpdate(BufferUpdate message) {
        Platform.runLater(()->{
            PersonalBoardController controller = (PersonalBoardController) controllerHandler.getController(Views.PERSONAL_BOARD);
            controller.bufferUpdate(message.getBufferUpdated());
        });
    }

    @Override
    public void handleDepotPositioningRequest(DepotPositioningRequest message) {
        Platform.runLater(()->{
            PersonalBoardController pbController = (PersonalBoardController) controllerHandler.getController(Views.PERSONAL_BOARD);
            pbController.setBufferLabel("Put those resources inside the depots");
            pbController.setUpResourceFromMarket(message.getResources());
            ControllerHandler.getInstance().changeView(Views.PERSONAL_BOARD);
        });
    }

    @Override
    public void handleWarehouseRemovingRequest(WarehouseRemovingRequest message) {
        Platform.runLater(()->{
            PersonalBoardController pbController = (PersonalBoardController) controllerHandler.getController(Views.PERSONAL_BOARD);
            pbController.setBufferLabel("Remove those resources from yours depots or strongbox");
            pbController.setUpWarehouseResourceRemoving(message.getResources());
            ControllerHandler.getInstance().changeView(Views.PERSONAL_BOARD);
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
                personalBoardController.resetLeaderDepots();
                personalBoardController.loadLeader(Client.getInstance().getModelOf(message.getUsername()).toModelData());
                personalBoardController.loadLeaderDepots(Client.getInstance().getModelOf(message.getUsername()).toModelData());
            }
        });
    }

    @Override
    public void handleDeckDevCardRemoving(RemoveDeckDevelopmentCard message) {
        super.handleDeckDevCardRemoving(message);
        //TODO single player
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
        if (message.isNormalDepot()) {
            Platform.runLater(() -> {
                PersonalBoardController personalBoardController = (PersonalBoardController) ControllerHandler.getInstance().getController(Views.PERSONAL_BOARD);
                if (message.getUsername().equals(personalBoardController.getCurrentShowed())) {
                    personalBoardController.resetStandardDepots();
                    personalBoardController.loadStandardDepots(Client.getInstance().getModelOf(message.getUsername()).toModelData());
                }
            });
        }else{
            Platform.runLater(()->{
                PersonalBoardController personalBoardController = (PersonalBoardController) ControllerHandler.getInstance().getController(Views.PERSONAL_BOARD);
                if(message.getUsername().equals(personalBoardController.getCurrentShowed())) {
                    personalBoardController.resetLeaderDepots();
                    personalBoardController.loadLeaderDepots(Client.getInstance().getModelOf(message.getUsername()).toModelData());
                }
            });
        }
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
        Platform.runLater(()->{
            String msg = "Winning condition has been reached, " +
                    "all players will play the last turn until the inkwell player !!";

            ControllerHandler.getInstance().getCurrentController().showCustomMessage(msg);
        });
    }

    @Override
    public void handleProductionSelectionCompleted() {
        Platform.runLater(()->{
            PersonalBoardController controller = (PersonalBoardController) ControllerHandler.getInstance()
                    .getController(Views.PERSONAL_BOARD);
            controller.endLocalProduction();
        });
    }
}
