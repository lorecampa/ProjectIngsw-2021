package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.GUI.controller.*;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.clientMessage.*;
import javafx.application.Platform;

import java.io.IOException;


public class GUIMessageHandler extends ClientMessageHandler {
    private final Client client = Client.getInstance();
    private final ControllerHandler controllerHandler = ControllerHandler.getInstance();

    /**
     * See {@link ClientMessageHandler#reconnect(ReconnectionMessage)}.
     */
    @Override
    public void reconnect(ReconnectionMessage message){
        try {
            super.reconnect(message);
            Platform.runLater(()-> controllerHandler.getCurrentController().showCustomMessage("We saved your main data, making sure you can disconnect and reconnect during the game!"));
        } catch (IOException e) {
            Platform.runLater(()-> controllerHandler.getCurrentController().showCustomMessage("Not able to save the file with your info to reconnect!"));
        }
    }
    /**
     * See {@link ClientMessageHandler#reconnectGameSetUp(ReconnectGameMessage)}.
     */
    @Override
    public void reconnectGameSetUp(ReconnectGameMessage message) {
        super.reconnectGameSetUp(message);
        Platform.runLater(()-> controllerHandler.changeView(Views.PERSONAL_BOARD));
    }
    /**
     * See {@link ClientMessageHandler#validReconnect(ConnectionMessage)}.
     */
    @Override
    public void validReconnect(ConnectionMessage message) {
        super.validReconnect(message);
        Platform.runLater(()->{
            WaitingController waitingController = (WaitingController) controllerHandler.getController(Views.WAITING);
            waitingController.setReconnectMessage(message.getMessage());
            controllerHandler.changeView(Views.WAITING);
        });
    }
    /**
     * See {@link ClientMessageHandler#strongboxUpdate(StrongboxUpdate)}.
     */
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
    /**
     * See {@link ClientMessageHandler#handleError(ErrorMessage)}.
     */
    @Override
    public void handleError(ErrorMessage message) {
        String error = (message.getErrorType() == null)?message.getCustomError():message.getErrorType().getMessage();
        if (Client.getInstance().getMyModel() != null)
            Client.getInstance().getMyModel().addErrorInLog(error);
        Platform.runLater(()-> controllerHandler.getCurrentController().showCustomMessage(error));
    }
    /**
     * See {@link ClientMessageHandler#connectNewUser(ConnectionMessage)}.
     */
    @Override
    public void connectNewUser(ConnectionMessage message) {
        Platform.runLater(()-> controllerHandler.getCurrentController().showCustomMessage(message.getMessage()));
    }

    /**
     * See {@link ClientMessageHandler#waitingPeople(ConnectionMessage)}.
     */
    @Override
    public void waitingPeople(ConnectionMessage message) {
        Platform.runLater(()->{
            WaitingController controller = (WaitingController) controllerHandler.getController(Views.WAITING);
            controller.setLobbyMessage();
            controllerHandler.changeView(Views.WAITING);
        });
    }
    /**
     * See {@link ClientMessageHandler#username(ConnectionMessage)}.
     */
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
    /**
     * See {@link ClientMessageHandler#newTurn(StarTurn)}.
     */
    @Override
    public void newTurn(StarTurn message) {
        super.newTurn(message);
        String msg = "Is "+ message.getUsername() + " turn";
        Platform.runLater(()-> controllerHandler.getCurrentController().showCustomMessage(msg));
    }
    /**
     * See {@link ClientMessageHandler#numberOfPlayer(ConnectionMessage)}.
     */
    @Override
    public void numberOfPlayer(ConnectionMessage message) {
        Platform.runLater(()->{
            controllerHandler.changeView(Views.SETUP);
            SetupController setupController = (SetupController) controllerHandler.getController(Views.SETUP);
            setupController.showNumOfPLayer();
        });
    }
    /**
     * See {@link ClientMessageHandler#connectInfo(ConnectionMessage)}.
     */
    @Override
    public void connectInfo(ConnectionMessage message) {
        Platform.runLater(()-> controllerHandler.getCurrentController().showCustomMessage(message.getMessage()));
    }


    /**
     * See {@link ClientMessageHandler#leaderSetUp(LeaderSetUpMessage)}.
     */
    @Override
    public void leaderSetUp(LeaderSetUpMessage message) {
        super.leaderSetUp(message);
        Platform.runLater(()->{
            PreGameSelectionController controller = (PreGameSelectionController) controllerHandler.getController(Views.PRE_MATCH);
            controllerHandler.changeView(Views.PRE_MATCH);
            controller.setUpLeaderImages(message.getLeaders());
        });
    }
    /**
     * See {@link ClientMessageHandler#mainMenu()}.
     */
    @Override
    public void mainMenu() {
        Platform.runLater(()->controllerHandler.changeView(Views.MAIN_MENU));
    }
    /**
     * See {@link ClientMessageHandler#startGame()}.
     */
    @Override
    public void startGame() {
        Platform.runLater(()->{
            controllerHandler.stopMusic();
            controllerHandler.changeView(Views.PERSONAL_BOARD);
        });
    }
    /**
     * See {@link ClientMessageHandler#gameSetUp(GameSetup)}.
     */
    @Override
    public void gameSetUp(GameSetup message) {
        super.gameSetUp(message);
    }
    /**
     * See {@link ClientMessageHandler#gameOver(GameOver)}.
     */
    @Override
    public void gameOver(GameOver message) {
        super.gameOver(message);
        Platform.runLater(()->{
            GameEndedController controller = (GameEndedController) controllerHandler.getController(Views.GAME_END);
            controller.setUpRanking(message.getPlayers());
            ControllerHandler.getInstance().changeView(Views.GAME_END);
        });
    }


    /**
     * See {@link ClientMessageHandler#anyConversionRequest(AnyConversionRequest)}.
     */
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
    /**
     * See {@link ClientMessageHandler#leaderDiscardUpdate(LeaderDiscard)}.
     */
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

    /**
     * See {@link ClientMessageHandler#marketUpdate(MarketUpdate)}.
     */
    @Override
    public void marketUpdate(MarketUpdate message) {
        super.marketUpdate(message);
        MarketController controller = (MarketController) ControllerHandler.getInstance().getController(Views.MARKET);
        Platform.runLater(controller::setUpAll);
    }


    /**
     * See {@link ClientMessageHandler#bufferUpdate(BufferUpdate)}.
     */
    @Override
    public void bufferUpdate(BufferUpdate message) {
        Platform.runLater(()->{
            PersonalBoardController controller = (PersonalBoardController) controllerHandler.getController(Views.PERSONAL_BOARD);
            controller.bufferUpdate(message.getBufferUpdated());
        });
    }
    /**
     * See {@link ClientMessageHandler#handleDepotPositioningRequest(DepotPositioningRequest)}.
     */
    @Override
    public void handleDepotPositioningRequest(DepotPositioningRequest message) {
        Platform.runLater(()->{
            PersonalBoardController personalBoardController = (PersonalBoardController) controllerHandler.getController(Views.PERSONAL_BOARD);
            personalBoardController.setBufferLabel("Put those resources inside the depots");
            ControllerHandler.getInstance().changeView(Views.PERSONAL_BOARD);
            personalBoardController.setUpResourceFromMarket(message.getResources());

        });
    }
    /**
     * See {@link ClientMessageHandler#handleWarehouseRemovingRequest(WarehouseRemovingRequest)}.
     */
    @Override
    public void handleWarehouseRemovingRequest(WarehouseRemovingRequest message) {
        Platform.runLater(()->{
            PersonalBoardController personalBoardController = (PersonalBoardController) controllerHandler.getController(Views.PERSONAL_BOARD);
            personalBoardController.setBufferLabel("Remove those resources from yours depots or strongbox");
            ControllerHandler.getInstance().changeView(Views.PERSONAL_BOARD);
            personalBoardController.setUpWarehouseResourceRemoving(message.getResources());

        });
    }
    /**
     * See {@link ClientMessageHandler#whiteMarbleConversion(WhiteMarbleConversionRequest)}.
     */
    @Override
    public void whiteMarbleConversion(WhiteMarbleConversionRequest message){
        Platform.runLater(()-> {
            PersonalBoardController personalBoardController = (PersonalBoardController) controllerHandler.getController(Views.PERSONAL_BOARD);
            personalBoardController.setBufferLabel("You have to convert " + message.getNumOfWhiteMarbleDrew() + " white marbles into concrete resources");
            ControllerHandler.getInstance().changeView(Views.PERSONAL_BOARD);
            personalBoardController.setUpMarbleConv();
        });
    }
    /**
     * See {@link ClientMessageHandler#faithTrackPositionIncreased(FaithTrackIncrement)}.
     */
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
    /**
     * See {@link ClientMessageHandler#popeFavorActivation(PopeFavorActivated)}.
     */
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
    /**
     * See {@link ClientMessageHandler#activeLeader(LeaderActivate)}.
     */
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
    /**
     * See {@link ClientMessageHandler#handleDeckDevCardRemoving(RemoveDeckDevelopmentCard)}.
     */
    @Override
    public void handleDeckDevCardRemoving(RemoveDeckDevelopmentCard message) {
        super.handleDeckDevCardRemoving(message);
    }
    /**
     * See {@link ClientMessageHandler#cardSlotUpdate(CardSlotUpdate)}.
     */
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
    /**
     * See {@link ClientMessageHandler#depotUpdate(DepotUpdate)}.
     */
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
    /**
     * See {@link ClientMessageHandler#depotLeaderUpdate(DepotLeaderUpdate)}.
     */
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

    /**
     * See {@link ClientMessageHandler#winningCondition()}.
     */
    @Override
    public void winningCondition() {
        Platform.runLater(()->{
            String msg = "Winning condition has been reached, " +
                    "all players will play the last turn until the inkwell player !!";

            ControllerHandler.getInstance().getCurrentController().showCustomMessage(msg);
        });
    }
    /**
     * See {@link ClientMessageHandler#handleProductionSelectionCompleted()}.
     */
    @Override
    public void handleProductionSelectionCompleted() {
        Platform.runLater(()->{
            PersonalBoardController controller = (PersonalBoardController) ControllerHandler.getInstance()
                    .getController(Views.PERSONAL_BOARD);
            controller.endLocalProduction();
        });
    }
}
