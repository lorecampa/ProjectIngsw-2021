package it.polimi.ingsw.message;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.bothMessage.ConnectionMessage;

public class ServerMessageHandler {
    Controller controller;

    public ServerMessageHandler() { }

    public void handleConnectionMessage(ConnectionMessage message){
        System.out.println(message.getType() + ": " + message.getMessage());
    }




}
