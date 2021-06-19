package it.polimi.ingsw.client;

import it.polimi.ingsw.client.data.CardDevData;
import it.polimi.ingsw.client.data.ModelData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.PingPongMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.clientMessage.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class ClientMessageHandler {
    private final Client client = Client.getInstance();

    /**
     * Handle ping pong message
     * */
    public void handlePingPong(){
        client.writeToStream(new PingPongMessage());
    }

    /**
     * Handle ErrorMessage, print the error and add to log of curr player
     * @param message to handle
     * */
    public abstract void handleError(ErrorMessage message);

    //Connection message handler
    /**
     * Connect new user info
     * @param message to handle
     * */
    public abstract void connectNewUser(ConnectionMessage message);

    /**
     * Show user that he is waiting for more player
     * @param message to handle
     * */
    public abstract void waitingPeople(ConnectionMessage message);

    /**
     * Ask user to insert a valid username
     * @param message to handle
     * */
    public abstract void username(ConnectionMessage message);

    /**
     * Ask user the number of player
     *      * @param message to handle
     * */
    public abstract void numberOfPlayer(ConnectionMessage message);

    /**
     * Tell player his reconnection is valid
     * * @param message to handle
     * */
    public void validReconnect(ConnectionMessage message){
        client.setMyName(message.getMessage());
    }

    /**
     * Tell player some info from server
     * @param message to handle
     * */
    public abstract void connectInfo(ConnectionMessage message);

    /**
     * Save data to reconnect
     * @param message to handle
     * */
    public void reconnect(ReconnectionMessage message) throws IOException {

        File file = new File(Client.getInstance().DATA_LAST_GAME);
        if (!file.exists()){
            boolean result = file.createNewFile();
            if (!result){
                throw new IOException();
            }
        }

        FileWriter myWriter = new FileWriter(file);
        myWriter.write(message.getMatchID() + "\n" + message.getClientID());
        myWriter.close();

    }

    /**
     * Re-Set up model data after reconnection
     * @param message to handle
     * */
    public void reconnectGameSetUp(ReconnectGameMessage message){
        client.setModels(message.getUsernames());
        client.setMarketData(message.getMarket());
        client.setDeckDevData(message.getDeckDev());
        client.setBaseProduction(message.getBaseProd());
        client.setInkwell();
        for (ModelData modelData : message.getModels()){
            client.setUpModel(modelData);
        }
        client.setMyName(message.getPlayerUsername());
        client.setState(ClientState.IN_GAME);
    }

    //MainMenu message handler
    /**
     * Show main menu
     * */
    public abstract void mainMenu();

    //StartTurn message handler
    /**
     * New turn
     * @param message to handle
     * */
    public void newTurn(StarTurn message){
        if(message.getUsername().equals(client.getMyName())){
            client.setState(ClientState.IN_GAME);
        }
        else{
            client.setState(ClientState.WAITING);
        }
    }

    //GameSetup message handler
    /**
     * Set up model data for all players
     * @param message to handle
     * */
    public void gameSetUp(GameSetup message){
        client.setModels(message.getUsernames());
        client.setMarketData(message.getMarket());
        client.setDeckDevData(message.getDeckDev());
        client.setFaithTrackData(message.getFaithTracks());
        client.setBaseProduction(message.getBaseProd());
        client.setInkwell();
    }

    //leaders message handler
    /**
     * Set up leaders in model data
     * @param message to handle
     * */
    public void leaderSetUp(LeaderSetUpMessage message){
        client.getModelOf(client.getMyName()).setLeaders(message.getLeaders());
    }

    //start match handler
    /**
     * Game is just started
     * */
    public abstract void startGame();

    //AnyConversionRequest message handler
    /**
     * Request of any from server,ask player conversions
     * @param message to handle
     * */
    public abstract void anyConversionRequest(AnyConversionRequest message);

    //DepotUpdate maessage handler
    /**
     * Update depots in model data
     * @param message to handle
     * */
    public void depotUpdate(DepotUpdate message){
        if(message.isNormalDepot()){
            client.getModelOf(message.getUsername()).setStandardDepotAt(message.getDepotIndex(), message.getDepot());
        }
        else{
            client.getModelOf(message.getUsername()).setLeaderDepotAt(message.getDepotIndex(), message.getDepot());
        }
    }

    //Strongbox message handler
    /**
     * Update strongbox in model data
     * @param message to handle
     * */
    public void strongboxUpdate(StrongboxUpdate message){
        client.getModelOf(message.getUsername()).setStrongbox(message.getStrongboxUpdated());
    }

    //leader discard message handler
    /**
     * Remove a leader in model data
     * @param message to handle
     * */
    public void leaderDiscardUpdate(LeaderDiscard message){
        if (message.getUsername().equals(client.getMyName())){
            client.getModelOf(client.getMyName()).removeLeaderAt(message.getLeaderIndex());
        }
    }

    //leaderActivate message handler
    /**
     * Add a leader of activate in model data
     * @param message to handle
     * */
    public void activeLeader(LeaderActivate message){
        if(message.getUsername().equals(client.getMyName())){
            client.getModelOf(client.getMyName()).setLeaders(message.getLeaders());
        }
        else{
            client.getModelOf(message.getUsername()).putAsActiveInLeader(message.getLeaders());
        }
    }

    //MarketUpdate message handler
    /**
     * Update market if someone used it
     * @param message to handle
     * */
    public void marketUpdate(MarketUpdate message){
        client.setMarketData(message.getMarket());
    }

    //CardSlotUpdate message handler
    /**
     * Update card slots in model data
     * @param message to handle
     * */
    public void cardSlotUpdate(CardSlotUpdate message){
        CardDevData card = client.getDeckDevData().getCard(message.getRowDeckDevelopment(), message.getColDeckDevelopment());
        client.getModelOf(message.getUsername()).addToCardSlot(message.getSlotIndex(), card);
    }

    //BufferUpdate message handler
    /**
     * Update of buffer to show
     * @param message to handle
     * */
    public abstract void bufferUpdate(BufferUpdate message);

    //ManageResourceRequest message handler
    /**
     * You have to insert some resources in depots
     * @param message to handle
     * */
    public abstract void handleDepotPositioningRequest(DepotPositioningRequest message);

    /**
     * You have to subtract some resource from depots
     * @param message to handle
     * */
    public abstract void handleWarehouseRemovingRequest(WarehouseRemovingRequest message);

    /**
     * Increase fait curr position in model data
     * @param message to handle
     * */
    public void faithTrackPositionIncreased(FaithTrackIncrement message){
        client.getModelOf(message.getUsername()).increaseCurrentPosOnFaithTrack();
    }

    /**
     * Pope favor activated
     * @param message to handle
     * */
    public void popeFavorActivation(PopeFavorActivated message){
        client.getModelOf(message.getUsername()).popeFavorActivation(message.getIdVaticanReport(),message.isDiscard());
    }

    //WhiteMarbleConversionRequest message handler
    /**
     * Ask for a white marble conversion request
     * @param message to handle
     * */
    public abstract void whiteMarbleConversion(WhiteMarbleConversionRequest message);

    //DepotLeaderUpdate message handler
    /**
     *Update the leader depot in the model data
     * @param message to handle
     * */
    public void depotLeaderUpdate(DepotLeaderUpdate message){
        for(ResourceData rs : message.getDepots()){
            if (message.isDiscard()){
                client.getModelOf(message.getUsername()).removeLeaderDepot(rs);
            }else{
                client.getModelOf(message.getUsername()).addLeaderDepot(rs);
            }
        }
    }


    //GameOver message handler
    /**
     * Game over, show end screen
     * @param message to handle
     * */
    public void gameOver(GameOver message){
        client.setState(ClientState.GAME_OVER);
    }

    //WinningCondition message handler
    /**
     * Notify player winning condition reached
     * */
    public abstract void winningCondition();

    //single player
    /**
     * Remove card from deck dev for single player only
     * @param message to handle
     * */
    public void handleDeckDevCardRemoving(RemoveDeckDevelopmentCard message){
        client.getDeckDevData().removeCardDevData(message.getRow(), message.getColumn());
    }

    /**
    * Notify player we insert the card he choose to produce
     *  */
    public abstract void handleProductionSelectionCompleted();


}
