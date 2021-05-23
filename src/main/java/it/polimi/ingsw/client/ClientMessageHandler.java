package it.polimi.ingsw.client;

import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.clientMessage.*;

public interface ClientMessageHandler {
    void handlePingPong();

    void handleError(ErrorMessage message);

    //Connection message handler
    void connectNewUser(ConnectionMessage message);

    void waitingPeople(ConnectionMessage message);

    void username(ConnectionMessage message);

    void numberOfPlayer(ConnectionMessage message);

    void validReconnect(ConnectionMessage message);

    void connectInfo(ConnectionMessage message);

    //Reconnect message handler
    void reconnect(ReconnectionMessage message);

    void reconnectGameSetUp(ReconnectGameMessage message);

    //MainMenu message handler
    void mainMenu();

    //StartTurn message handler
    void newTurn(StarTurn message);

    //GameSetup message handler
    void gameSetUp(GameSetup message);

    //leaders message handler
    void leaderSetUp(LeaderSetUpMessage message);

    //start match handler
    void startGame();

    //AnyConversionRequest message handler
    void anyConversionRequest(AnyConversionRequest message);

    //DepotUpdate maessage handler
    void depotUpdate(DepotUpdate message);

    //Strongbox message handler
    void strongboxUpdate(StrongboxUpdate message);

    //leader discard message handler
    void discardUpdate(LeaderDiscard message);

    //leaderActivate message handler
    void activeLeader(LeaderActivate message);

    //MarketUpdate message handler
    void marketUpdate(MarketUpdate message);

    //CardSlotUpdate message handler
    void cardSlotUpdate(CardSlotUpdate message);

    //BufferUpdate message handler
    void bufferUpdate(BufferUpdate message);

    //ManageResourceRequest message handler
    void manageResourceRequest(ManageResourcesRequest message);

    void faithTrackPositionIncreased(FaithTrackIncrement message);

    void popeFavorActivation(PopeFavorActivated message);

    //WhiteMarbleConversionRequest message handler
    void whiteMarbleConversion(WhiteMarbleConversionRequest message);

    //DepotLeaderUpdate message handler
    void depotLeaderUpdate(DepotLeaderUpdate message);

    //GameOver message handler
    void gameOver(GameOver message);

    //WinningCondition message handler
    void winningCondition();

    //single player
    void handleDeckDevCardRemoving(RemoveDeckDevelopmentCard message);


    //Print
    void printFaith(String username);
    void printCardSlots(String username);
    void printResources(String username);
    void printLeader(String username);
}
