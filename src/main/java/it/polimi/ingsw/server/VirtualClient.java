package it.polimi.ingsw.server;

import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.bothArchitectureMessage.*;
import it.polimi.ingsw.message.clientMessage.AnyConversionRequest;
import it.polimi.ingsw.message.clientMessage.FaithTrackIncrement;
import it.polimi.ingsw.message.clientMessage.NewTurn;
import it.polimi.ingsw.message.clientMessage.PopeFavorActivated;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.observer.CardManagerObserver;
import it.polimi.ingsw.observer.FaithTrackObserver;
import it.polimi.ingsw.observer.ModelObserver;
import it.polimi.ingsw.observer.ResourceManagerObserver;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class VirtualClient implements ModelObserver, ResourceManagerObserver,
        FaithTrackObserver, CardManagerObserver {

    private final int id;
    private String username;
    private final ClientConnectionHandler client;
    private Controller controller;
    private final Match match;
    private boolean ready;

    public VirtualClient(int id, String username,
                         ClientConnectionHandler clientConnectionHandler,
                         Match match) {
        this.id = id;
        this.username = username;
        this.client= clientConnectionHandler;
        this.match = match;
        this.client.setVirtualClient(this);
        this.ready = false;


        //TODO it is null pointer exception now
        //maybe to do here
        controller.registerToAllObservable(this);
    }

    public ClientConnectionHandler getClient() { return client; }

    public int getId() {
        return id;
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
                "id=" + id +
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



    //RESOURCE MANAGER OBSERVER
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
}
