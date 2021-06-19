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
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represent a Client in the Server.
 */
public class VirtualClient implements ModelObserver, ResourceManagerObserver,
        FaithTrackObserver, CardManagerObserver, MarketObserver {

    private String username;
    private ClientConnectionHandler client;
    private final Match match;
    private boolean ready;
    private boolean reconnected = false;
    private int clientID;

    /**
     * Construct a new Virtual Client based on specific param.
     * @param username the username of the client.
     * @param clientConnectionHandler the handler of the client.
     * @param match the match of the client.
     */
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

    /**
     * Construct a new Virtual Client based on specific param.
     * @param username the username of the client.
     * @param match the match of the client.
     */
    public VirtualClient(String username, Match match) {
        this.username = username;
        this.match = match;
        this.ready = false;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    /**
     * Construct a new Virtual Client based on specific param.
     * @param username the username of the client.
     * @param match the match of the client.
     * @param clientID the id of the client.
     */
    public VirtualClient(String username, Match match, int clientID){
        this.username = username;
        this.match = match;
        this.ready = false;
        this.clientID = clientID;
    }

    /**
     * Add a message to match's log.
     * @param message the message to add.
     */
    public void addToLog(ServerMessage message){
        SimpleDateFormat formatter=new SimpleDateFormat("HH:mm:ss");

        String s= formatter.format( new Date())
                +" - ["+username+"]"
                +message;
        match.addToLog(s);
    }

    /**
     * Return the Connection Handler of the client.
     * @return the Connection Handler of the client.
     */
    public ClientConnectionHandler getClient() { return client; }

    /**
     * Set the Connection Handler of the client.
     * @param client the Connection Handler of the client.
     */
    public void setClient(ClientConnectionHandler client) { this.client = client; }

    /**
     * Return the id of the client.
     * @return the id of the client.
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * Return true if it's reconnected.
     * @return true if it's reconnected.
     */
    public boolean isReconnected() {
        return reconnected;
    }

    /**
     * Set reconnected to a new value.
     * @param reconnected the new value of reconnected
     */
    public void setReconnected(boolean reconnected) {
        this.reconnected = reconnected;
    }

    /**
     * Return the username of the client.
     * @return the username of the client.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the client.
     * @param username the username of the client.
     */
    public void setUsername(String username) {
        this.username = username;
        this.ready = true;
    }

    /**
     * Return true if it's ready.
     * @return true if it's ready.
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Return the match of the client.
     * @return the match of the client.
     */
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

    /**
     * Send the leaderboard to the player.
     * @param matchRanking the leaderboard of the match.
     */
    @Override
    public void weHaveAWinner(Map<Float, String> matchRanking) {
        match.sendSinglePlayer( username,new GameOver(matchRanking));
    }

    /**
     * Send a massage that inform the player that the winning condition has been reached.
     */
    @Override
    public void winningCondition() {
        match.sendSinglePlayer( username,new WinningCondition());
    }

    //CREATION EFFECTS

    /**
     * Send the new depot update to the player.
     * @param depots the depots of the leader.
     * @param isDiscard true if the depot is discarded.
     */
    @Override
    public void updateLeaderDepot(ArrayList<Depot> depots, boolean isDiscard){
        match.sendAllPlayers(new DepotLeaderUpdate(depots.stream()
                .map(x-> new ResourceData(x.getResourceType(), x.getMaxStorable()))
                .collect(Collectors.toCollection(ArrayList::new)), isDiscard,username));
    }


    //MODEL OBSERVER

    /**
     * Send the update of new turn to the player.
     * @param nextPlayer the new player that has the turn.
     */
    @Override
    public void currentPlayerChange(String nextPlayer) {
        match.sendSinglePlayer(username, new StarTurn(nextPlayer));
    }

    /**
     * Send the update of the deck development to the player.
     * @param row the row of the removed card.
     * @param column the column of the removed card.
     */
    @Override
    public void removeDeckDevelopmentSinglePlayer(int row, int column) {
        match.sendSinglePlayer(getUsername(), new RemoveDeckDevelopmentCard(row, column));
    }


    //RESOURCE MANAGER OBSERVER

    /**
     * Send the request of depot positioning to the player.
     * @param resources the resources from market.
     */
    @Override
    public void depotPositioningRequest(ArrayList<Resource> resources) {
        match.sendSinglePlayer(getUsername(),
                new DepotPositioningRequest(resources.stream()
                        .map(Resource::toClient)
                        .collect(Collectors.toCollection(ArrayList::new))));
    }

    /**
     * Send the request of warehouse removing to the player.
     * @param resources the resources to remove.
     */
    @Override
    public void warehouseRemovingRequest(ArrayList<Resource> resources) {
        match.sendSinglePlayer(getUsername(),
                new WarehouseRemovingRequest(resources.stream()
                        .map(Resource::toClient)
                        .collect(Collectors.toCollection(ArrayList::new))));
    }

    /**
     * Send the update of the buffer to the player.
     * @param resources the resource buffer.
     */
    @Override
    public void bufferUpdate(ArrayList<Resource> resources) {
        match.sendSinglePlayer(getUsername(),
                new BufferUpdate(resources.stream()
                        .map(Resource::toClient)
                        .collect(Collectors.toCollection(ArrayList::new))));
    }

    /**
     * Send the request of conversion to the player.
     * @param optionOfConversion the option that the player has to convert.
     * @param optionOfDiscount the discount available to the player.
     * @param numOfAny the number of ANY to convert.
     */
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

    /**
     * Send the update to the player.
     */
    @Override
    public void productionCardSelectionCompleted() {
        match.sendSinglePlayer(getUsername(), new ConnectionMessage(ConnectionType.SEMI_PRODUCTION_ACK));
    }

    /**
     * Send the request of conversion to the player.
     * @param numOfAny the number of ANY to convert.
     */
    @Override
    public void anyProductionProfitRequest(int numOfAny) {
        match.sendSinglePlayer(getUsername(), new AnyConversionRequest(numOfAny));
    }

    //Faith Track OBSERVER

    /**
     * Send the update of the position to the player.
     */
    @Override
    public void positionIncrease() {
        match.sendAllPlayers(new FaithTrackIncrement(username));
    }


    /**
     * Send the update of pope favor reached to the player.
     * @param idVaticanReport the id of the vatican report.
     * @param isDiscard true if the player has obtained the pope favor.
     */
    @Override
    public void popeFavorReached(int idVaticanReport, boolean isDiscard) {
        match.sendAllPlayers(new PopeFavorActivated(idVaticanReport, isDiscard, username));
    }


    //Market

    /**
     * Send the update market tray to the player.
     * @param marketTray the marketTray of the market.
     * @param lastMarble the new marble to insert.
     */
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

    /**
     * Send the update card slot to the player.
     * @param indexCardSlot the index of the card slot.
     * @param rowDeckDevelopment the row of the card in the deck development.
     * @param colDeckDevelopment the column of the card in the deck development.
     */
    @Override
    public void cardSlotUpdate(int indexCardSlot, int rowDeckDevelopment, int colDeckDevelopment) {
        match.sendAllPlayers(
                new CardSlotUpdate(rowDeckDevelopment, colDeckDevelopment, indexCardSlot, username));
    }

    /**
     * Send the update leaders to the player.
     * @param leaders the ArrayList of leaders
     */
    @Override
    public void leaderActivated(ArrayList<Leader> leaders) {
        match.sendAllPlayers(new LeaderActivate(leaders.stream().map(Leader::toCardLeaderData).collect(Collectors.toCollection(ArrayList::new)), username));
    }

    /**
     * Send the index of the discarded leader to the player.
     * @param leaderIndex the index of the leader discarded.
     */
    @Override
    public void leaderDiscard(int leaderIndex) {
        match.sendAllPlayers(new LeaderDiscard(leaderIndex, username));
    }

    //Warehouse updating

    /**
     * Send the update strongbox to the player.
     * @param strongboxUpdated the updated strongbox.
     */
    @Override
    public void strongboxUpdate(ArrayList<Resource> strongboxUpdated) {
        match.sendAllPlayers(
                new StrongboxUpdate(strongboxUpdated.stream()
                        .map(Resource::toClient)
                        .collect(Collectors.toCollection(ArrayList::new)), username));
    }

    /**
     * Send the depot update to the player.
     * @param depotUpdated the updated depot.
     * @param index the index of the depot.
     * @param isNormalDepot true if it's not a leader depot.
     */
    @Override
    public void depotUpdate(Resource depotUpdated, int index, boolean isNormalDepot) {
        match.sendAllPlayers(
                new DepotUpdate(depotUpdated.toClient(), index, isNormalDepot, username)
        );
    }
}
