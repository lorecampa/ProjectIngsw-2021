package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.clientMessage.*;
import javafx.application.Platform;

public class GUIMessageHandler implements ClientMessageHandler {
    @Override
    public void handlePingPong() {

    }

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
        Platform.runLater(()->{
            ControllerHandler.getInstance().getSecondaryController().activate();
            ControllerHandler.getInstance().getSecondaryController().numOfPlayerRequest();});

    }

    @Override
    public void validReconnect(ConnectionMessage message) {

    }

    @Override
    public void connectInfo(ConnectionMessage message) {

    }

    @Override
    public void reconnect(ReconnectionMessage message) {

    }

    @Override
    public void reconnectGameSetUp(ReconnectGameMessage message) {

    }

    @Override
    public void mainMenu() {

    }

    @Override
    public void newTurn(StarTurn message) {

    }

    @Override
    public void gameSetUp(GameSetup message) {

    }

    @Override
    public void leaderSetUp(LeaderSetUpMessage message) {

    }

    @Override
    public void startGame() {

    }

    @Override
    public void anyConversionRequest(AnyConversionRequest message) {

    }

    @Override
    public void depotUpdate(DepotUpdate message) {

    }

    @Override
    public void strongboxUpdate(StrongboxUpdate message) {

    }

    @Override
    public void discardUpdate(LeaderDiscard message) {

    }

    @Override
    public void activeLeader(LeaderActivate message) {

    }

    @Override
    public void marketUpdate(MarketUpdate message) {

    }

    @Override
    public void cardSlotUpdate(CardSlotUpdate message) {

    }

    @Override
    public void bufferUpdate(BufferUpdate message) {

    }

    @Override
    public void manageResourceRequest(ManageResourcesRequest message) {

    }

    @Override
    public void faithTrackPositionIncreased(FaithTrackIncrement message) {

    }

    @Override
    public void popeFavorActivation(PopeFavorActivated message) {

    }

    @Override
    public void whiteMarbleConversion(WhiteMarbleConversionRequest message) {

    }

    @Override
    public void depotLeaderUpdate(DepotLeaderUpdate message) {

    }

    @Override
    public void gameOver(GameOver message) {

    }

    @Override
    public void winningCondition() {

    }

    @Override
    public void handleDeckDevCardRemoving(RemoveDeckDevelopmentCard message) {

    }

    @Override
    public void printFaith(String username) {

    }

    @Override
    public void printCardSlots(String username) {

    }

    @Override
    public void printResources(String username) {

    }

    @Override
    public void printLeader(String username) {

    }
}
