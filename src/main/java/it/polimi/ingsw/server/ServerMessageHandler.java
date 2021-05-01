package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.bothMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothMessage.ConnectionType;
import it.polimi.ingsw.server.ClientConnectionHandler;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.message.bothMessage.PingPongMessage;

public class ServerMessageHandler {
    Controller controller;
    Server server;
    ClientConnectionHandler client;
    boolean registration;

    public ServerMessageHandler(Server server, ClientConnectionHandler client) {
        this.server = server;
        this.client = client;
        this.registration = true;
    }

    public void handleConnectionMessage(ConnectionMessage message){
        System.out.println(message.getType() + ": " + message.getMessage());
    }

    public void handleFirstContact(ConnectionMessage message){
        if(!registration) return;
        server.putInLobby(client);
    }

    public void handleMatchCreation(ConnectionMessage message){
        if (!registration) return;
        server.createMatch(message.getNum(),client);
    }

    public void handleUsernameInput(ConnectionMessage message){
        if (!registration) return;
    }

    public void handlePingPong(PingPongMessage message){
        //TODO
    }

}
