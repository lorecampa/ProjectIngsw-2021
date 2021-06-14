package it.polimi.ingsw.client;

import it.polimi.ingsw.client.data.CardDevData;
import it.polimi.ingsw.client.data.ModelData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.PingPongMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.clientMessage.*;

import java.io.FileWriter;
import java.io.IOException;

public abstract class ClientMessageHandler {
    private final Client client = Client.getInstance();


    public void handlePingPong(){
        client.writeToStream(new PingPongMessage());
    }

    public abstract void handleError(ErrorMessage message);

    //Connection message handler
    public abstract void connectNewUser(ConnectionMessage message);

    public abstract void waitingPeople(ConnectionMessage message);

    public abstract void username(ConnectionMessage message);

    public abstract void numberOfPlayer(ConnectionMessage message);

    public void validReconnect(ConnectionMessage message){
        client.setMyName(message.getMessage());
    }

    public abstract void connectInfo(ConnectionMessage message);

    //Reconnect message handler
    public void reconnect(ReconnectionMessage message) throws IOException {
        //TODO: riattivare l'if solo se siamo sicuri di avere i jar in cartelle diverse prima dell'esecuzione
        /*
            File myObj = new File(client.getNameFile());
            if (myObj.createNewFile()) {
            */
        FileWriter myWriter = new FileWriter(client.getNameFile());
        myWriter.write(message.getMatchID()+"\n");
        myWriter.write(message.getClientID()+"");
        myWriter.close();
            /*
            } else {
                PrintAssistant.instance.errorPrint("Not able to save the file with your info to reconnect!");
            }
            */
    }

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
    public abstract void mainMenu();

    //StartTurn message handler
    public void newTurn(StarTurn message){
        if(message.getUsername().equals(client.getMyName())){
            client.setState(ClientState.IN_GAME);
        }
        else{
            client.setState(ClientState.WAITING);
        }
    }

    //GameSetup message handler
    public void gameSetUp(GameSetup message){
        client.setModels(message.getUsernames());
        client.setMarketData(message.getMarket());
        client.setDeckDevData(message.getDeckDev());
        client.setFaithTrackData(message.getFaithTracks());
        client.setBaseProduction(message.getBaseProd());
        client.setInkwell();
    }

    //leaders message handler
    public void leaderSetUp(LeaderSetUpMessage message){
        client.getModelOf(client.getMyName()).setLeaders(message.getLeaders());
    }

    //start match handler
    public abstract void startGame();

    //AnyConversionRequest message handler
    public abstract void anyConversionRequest(AnyConversionRequest message);

    //DepotUpdate maessage handler
    public void depotUpdate(DepotUpdate message){
        if(message.isNormalDepot()){
            client.getModelOf(message.getUsername()).setStandardDepotAt(message.getDepotIndex(), message.getDepot());
        }
        else{
            client.getModelOf(message.getUsername()).setLeaderDepotAt(message.getDepotIndex(), message.getDepot());
        }
    }

    //Strongbox message handler
    public void strongboxUpdate(StrongboxUpdate message){
        client.getModelOf(message.getUsername()).setStrongbox(message.getStrongboxUpdated());
    }

    //leader discard message handler
    public void leaderDiscardUpdate(LeaderDiscard message){
        if (message.getUsername().equals(client.getMyName())){
            client.getModelOf(client.getMyName()).removeLeaderAt(message.getLeaderIndex());
        }
    }

    //leaderActivate message handler
    public void activeLeader(LeaderActivate message){
        if(message.getUsername().equals(client.getMyName())){
            client.getModelOf(client.getMyName()).setLeaders(message.getLeaders());
        }
        else{
            client.getModelOf(message.getUsername()).putAsActiveInLeader(message.getLeaders());
        }
    }

    //MarketUpdate message handler
    public void marketUpdate(MarketUpdate message){
        client.setMarketData(message.getMarket());
    }

    //CardSlotUpdate message handler
    public void cardSlotUpdate(CardSlotUpdate message){
        CardDevData card = client.getDeckDevData().getCard(message.getRowDeckDevelopment(), message.getColDeckDevelopment());
        client.getModelOf(message.getUsername()).addToCardSlot(message.getSlotIndex(), card);
    }

    //BufferUpdate message handler
    public abstract void bufferUpdate(BufferUpdate message);

    //ManageResourceRequest message handler
    public abstract void handleDepotPositioningRequest(DepotPositioningRequest message);

    public abstract void handleWarehouseRemovingRequest(WarehouseRemovingRequest message);

    public void faithTrackPositionIncreased(FaithTrackIncrement message){
        client.getModelOf(message.getUsername()).increaseCurrentPosOnFaithTrack();
    }

    public void popeFavorActivation(PopeFavorActivated message){
        client.getModelOf(message.getUsername()).popeFavorActivation(message.getIdVaticanReport(),message.isDiscard());
    }

    //WhiteMarbleConversionRequest message handler
    public abstract void whiteMarbleConversion(WhiteMarbleConversionRequest message);

    //DepotLeaderUpdate message handler
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
    public void gameOver(GameOver message){
        client.setState(ClientState.GAME_OVER);
    }

    //WinningCondition message handler
    public abstract void winningCondition();

    //single player
    public void handleDeckDevCardRemoving(RemoveDeckDevelopmentCard message){
        client.getDeckDevData().removeCardDevData(message.getRow(), message.getColumn());
    }


    public abstract void handleProductionSelectionCompleted();


}
