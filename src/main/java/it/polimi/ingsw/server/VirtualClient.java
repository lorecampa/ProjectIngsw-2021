package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.bothMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothMessage.ConnectionType;
import it.polimi.ingsw.observer.ModelObserver;

public class VirtualClient implements ModelObserver{
    int id;
    String username;
    ClientConnectionHandler client;
    Controller controller;

    public VirtualClient(int id, String username,
                         ClientConnectionHandler clientConnectionHandler) {
        this.id = id;
        this.username = username;
        this.client= clientConnectionHandler;
    }



    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
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
