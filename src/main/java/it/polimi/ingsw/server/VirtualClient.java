package it.polimi.ingsw.server;

import it.polimi.ingsw.client.data.ColorData;
import it.polimi.ingsw.client.data.MarketData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;
import it.polimi.ingsw.message.clientMessage.*;
import it.polimi.ingsw.message.serverMessage.ServerMessage;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.personalBoard.market.Marble;
import it.polimi.ingsw.model.personalBoard.resourceManager.Depot;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.observer.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class VirtualClient implements ModelObserver, ResourceManagerObserver,
        FaithTrackObserver, CardManagerObserver, MarketObserver {

    private String username;
    private ClientConnectionHandler client;
    private final Match match;
    private boolean ready;
    private boolean reconnected = false;
    private int clientID;

    public VirtualClient(String username,
                         ClientConnectionHandler clientConnectionHandler,
                         Match match) {

        this.username = username;
        this.client= clientConnectionHandler;
        this.match = match;
        this.client.setVirtualClient(this);
        this.ready = false;
        this.clientID = client.getClientID();
    }

    public VirtualClient(String username, Match match) {
        this.username = username;
        this.match = match;
        this.ready = false;
    }
    public VirtualClient(String username, Match match, int clientID){
        this.username = username;
        this.match = match;
        this.ready = false;
        this.clientID = clientID;

    }


    public void addToLog(ServerMessage msg){
        SimpleDateFormat formatter=new SimpleDateFormat("HH:mm:ss");

        String s= formatter.format( new Date())
                +" - ["+username+"]"
                +msg;
        match.addToLog(s);
    }

    public ClientConnectionHandler getClient() { return client; }

    public void setClient(ClientConnectionHandler client) { this.client = client; }

    public int getClientID() {
        return clientID;
    }

    public boolean isReconnected() {
        return reconnected;
    }

    public void setReconnected(boolean reconnected) {
        this.reconnected = reconnected;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        this.ready = true;
    }

    public boolean isReady() {
        return ready;
    }

    public Match getMatch() {
        return match;
    }

    @Override
    public String toString() {
        return "VirtualClient{" +
                ", username='" + username + '\'' +
                ", clientHandler=" + client +
                '}';
    }



    //OBSERVER IMPLEMENTATION


    //WINNER
    @Override
    public void weHaveAWinner(Map<Integer, String> matchRanking) {
        match.sendSinglePlayer( username,new GameOver(matchRanking));
    }

    @Override
    public void winningCondition() {
        match.sendSinglePlayer( username,new WinningCondition());
    }

    //CREATION EFFECTS

    @Override
    public void updateLeaderDepot(ArrayList<Depot> depots, boolean isDiscard){
        match.sendAllPlayers(new DepotLeaderUpdate(depots.stream()
                .map(x-> new ResourceData(x.getResourceType(), x.getMaxStorable()))
                .collect(Collectors.toCollection(ArrayList::new)), isDiscard,username));
    }


    //MODEL OBSERVER
    @Override
    public void currentPlayerChange(String nextPlayer) {
        match.sendSinglePlayer(username, new StarTurn(nextPlayer));
    }


    @Override
    public void removeDeckDevelopmentSinglePlayer(int row, int column) {
        match.sendSinglePlayer(getUsername(), new RemoveDeckDevelopmentCard(row, column));
    }

    //RESOURCE MANAGER OBSERVER


    @Override
    public void depotPositioningRequest(ArrayList<Resource> resources) {
        match.sendSinglePlayer(getUsername(),
                new DepotPositioningRequest(resources.stream()
                        .map(Resource::toClient)
                        .collect(Collectors.toCollection(ArrayList::new))));
    }

    @Override
    public void warehouseRemovingRequest(ArrayList<Resource> resources) {
        match.sendSinglePlayer(getUsername(),
                new WarehouseRemovingRequest(resources.stream()
                        .map(Resource::toClient)
                        .collect(Collectors.toCollection(ArrayList::new))));
    }

    @Override
    public void bufferUpdate(ArrayList<Resource> resources) {
        match.sendSinglePlayer(getUsername(),
                new BufferUpdate(resources.stream()
                        .map(Resource::toClient)
                        .collect(Collectors.toCollection(ArrayList::new))));
    }

    @Override
    public void anyRequirementConversionRequest(ArrayList<Resource> optionOfConversion, ArrayList<Resource> optionOfDiscount, int numOfAny) {
        match.sendSinglePlayer(getUsername(),
                new AnyConversionRequest(
                        optionOfConversion.stream().map(Resource::toClient)
                                .collect(Collectors.toCollection(ArrayList::new)),
                        optionOfDiscount.stream().map(Resource::toClient)
                                .collect(Collectors.toCollection(ArrayList<ResourceData>::new)),
                        numOfAny));
    }

    @Override
    public void productionCardSelectionCompleted() {
        match.sendSinglePlayer(getUsername(), new ConnectionMessage(ConnectionType.SEMI_PRODUCTION_ACK));
    }

    @Override
    public void anyProductionProfitRequest(int numOfAny) {
        match.sendSinglePlayer(getUsername(), new AnyConversionRequest(numOfAny));
    }

    //Faith Track OBSERVER
    @Override
    public void positionIncrease() {
        match.sendAllPlayers(new FaithTrackIncrement(username));
    }



    @Override
    public void popeFavorReached(int idVaticanReport, boolean isDiscard) {
        match.sendAllPlayers(new PopeFavorActivated(idVaticanReport, isDiscard, username));
    }


    //Market

    @Override
    public void marketTrayChange(ArrayList<ArrayList<Marble>> marketTray, Marble lastMarble) {
        ArrayList<ArrayList<ColorData>> marketTrayToClient = new ArrayList<>();
        for (ArrayList<Marble> marbles : marketTray) {
            ArrayList<ColorData> marketCol = new ArrayList<>();
            for (Marble marble : marbles) {
                marketCol.add(marble.getColorData());
            }
            marketTrayToClient.add(marketCol);
        }

        match.sendAllPlayers(new MarketUpdate(new MarketData(marketTrayToClient, lastMarble.getColorData(), marketTrayToClient.size(), marketTrayToClient.get(0).size())));
    }


    //Card Manager

    @Override
    public void cardSlotUpdate(int indexCardSlot, int rowDeckDevelopment, int colDeckDevelopment) {
        match.sendAllPlayers(
                new CardSlotUpdate(rowDeckDevelopment, colDeckDevelopment, indexCardSlot, username));
    }

    @Override
    public void leaderActivated(ArrayList<Leader> leaders) {
        match.sendAllPlayers(new LeaderActivate(leaders.stream().map(Leader::toCardLeaderData).collect(Collectors.toCollection(ArrayList::new)), username));
    }

    @Override
    public void leaderDiscard(int leaderIndex) {
        match.sendAllPlayers(new LeaderDiscard(leaderIndex, username));
    }

    //Warehouse updating

    @Override
    public void strongboxUpdate(ArrayList<Resource> strongboxUpdated) {
        match.sendAllPlayers(
                new StrongboxUpdate(strongboxUpdated.stream()
                        .map(Resource::toClient)
                        .collect(Collectors.toCollection(ArrayList::new)), username));
    }

    @Override
    public void depotUpdate(Resource depotUpdated, int index, boolean isLeaderDepot) {
        match.sendAllPlayers(
                new DepotUpdate(depotUpdated.toClient(), index, isLeaderDepot, username)
        );
    }


}
