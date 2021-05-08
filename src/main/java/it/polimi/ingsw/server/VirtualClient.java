package it.polimi.ingsw.server;

import it.polimi.ingsw.client.data.ColorData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.bothArchitectureMessage.*;
import it.polimi.ingsw.message.clientMessage.*;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.observer.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class VirtualClient implements ModelObserver, ResourceManagerObserver,
        FaithTrackObserver, CardManagerObserver, MarketObserver {

    private String username;
    private ClientConnectionHandler client;
    private Controller controller;
    private final Match match;
    private boolean ready;

    public VirtualClient(String username,
                         ClientConnectionHandler clientConnectionHandler,
                         Match match) {

        this.username = username;
        this.client= clientConnectionHandler;
        this.match = match;
        this.client.setVirtualClient(this);
        this.ready = false;


        //TODO it is null pointer exception now
        //maybe to do here
        //controller.registerToAllObservable(this);
    }

    public ClientConnectionHandler getClient() { return client; }

    public void setClient(ClientConnectionHandler client) { this.client = client; }

    public int getClientID() {
        return client.getClientID();
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



    //MODEL OBSERVER
    @Override
    public void currentPlayerChange() {
        match.sendAllPlayers(new NewTurn());
    }


    @Override
    public void removeDeckDevelopmentSinglePlayer(int row, int column) {
        client.writeToStream(new RemoveDeckDevelopmentCard(row, column));
    }

    //RESOURCE MANAGER OBSERVER


    @Override
    public void bufferUpdate(ArrayList<Resource> resources) {
        match.sendSinglePlayer(getUsername(),
                new BufferUpdate(resources.stream()
                        .map(Resource::toClient)
                        .collect(Collectors.toCollection(ArrayList::new))));
    }

    @Override
    public void depotModify(Resource resource, int depotIndex, boolean isDepotLeader) {

        match.sendAllPlayers(new DepotModify(depotIndex, resource.toClient(), isDepotLeader, username));
    }

    @Override
    public void depotSwitch(int from, int to, boolean isToLeader) {
        match.sendAllPlayers(new DepotSwitch(from, to, isToLeader , username));
    }

    @Override
    public void strongboxModify(Resource resource, boolean isAdd) {
        match.sendAllPlayers(new StrongboxModify(resource.toClient(), isAdd, username));

    }

    @Override
    public void anyConversionRequest(ArrayList<Resource> optionOfConversion,
                                     ArrayList<Resource> optionOfDiscount,
                                     int numOfAny, boolean isProduction) {

        client.writeToStream(new AnyConversionRequest(
                optionOfConversion.stream().map(Resource::toClient).collect(Collectors.toCollection(ArrayList::new)),
                optionOfDiscount.stream().map(Resource::toClient).collect(Collectors.toCollection(ArrayList<ResourceData>::new)),
                numOfAny)
        );

    }

    //Faith Track OBSERVER
    @Override
    public void positionIncrease() {
        match.sendAllPlayers(new FaithTrackIncrement(1, username));
    }


    @Override
    public void popeFavorReached(int idVaticanReport, boolean isDiscard) {
        match.sendAllPlayers(new PopeFavorActivated(idVaticanReport, isDiscard, username));
    }


    //Market


    @Override
    public void marketTrayChange(ArrayList<ColorData> sequenceUpdated,
                                 ColorData lastMarble, int selection, boolean isRow) {


    }
}
