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
    VirtualClient virtualClient;
    HandlerState state;

    public ServerMessageHandler(Server server, ClientConnectionHandler client) {
        this.server = server;
        this.client = client;
        this.state = HandlerState.FIRST_CONTACT;
    }

    public void setVirtualClient(VirtualClient virtualClient) {
        this.virtualClient = virtualClient;
    }

    public void setState(HandlerState state){
        this.state = state;
    }

    public void handleConnectionMessage(ConnectionMessage message){
        System.out.println(message.getType() + ": " + message.getMessage());
    }

    public void handleFirstContact(ConnectionMessage message){
        if(state != HandlerState.FIRST_CONTACT) return;
        server.putInLobby(client);
    }

    public void handleMatchCreation(ConnectionMessage message){
        if (state != HandlerState.NUM_OF_PLAYER) return;
        server.createMatch(message.getNum(),client);
    }

    public void handleUsernameInput(ConnectionMessage message){
        if (state != HandlerState.USERNAME) return;
        virtualClient.getMatch().setPlayerUsername(virtualClient, message.getMessage());
    }

    public void handlePingPong(PingPongMessage message){
        //TODO
    }

}
