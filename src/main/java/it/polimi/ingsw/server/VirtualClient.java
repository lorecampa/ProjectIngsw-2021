package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.ConnectionMessage;
import it.polimi.ingsw.message.ConnectionType;
import it.polimi.ingsw.observer.ModelObserver;

public class VirtualClient implements ModelObserver{
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




    //MODEL OBSERVER IMPLEMENTATION
    @Override
    public void currentPlayerChange() {
        //TODO send to all player a change
        client.writeToStream(new ConnectionMessage(ConnectionType.INFO, "Current player has changed"));
    }

    @Override
    public void discardLeader() {
    }

    @Override
    public void vaticanReportReached(int idVR) {

    }

    @Override
    public void discardResources(int numResources) {

    }
}
