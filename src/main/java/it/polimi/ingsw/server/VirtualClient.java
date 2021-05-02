package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.bothMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothMessage.ConnectionType;
import it.polimi.ingsw.observer.ModelObserver;

public class VirtualClient implements ModelObserver{
    private final int id;
    private String username;
    private final ClientConnectionHandler client;
    private Controller controller;
    private Match match;

    public VirtualClient(int id, String username,
                         ClientConnectionHandler clientConnectionHandler,
                         Match match) {
        this.id = id;
        this.username = username;
        this.client= clientConnectionHandler;
        this.match = match;
        this.client.setVirtualClient(this);
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
}
