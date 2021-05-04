package it.polimi.ingsw.client;


import it.polimi.ingsw.message.ConnectionMessage;
import it.polimi.ingsw.message.ConnectionType;
import it.polimi.ingsw.message.PingPongMessage;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;

import java.util.ArrayList;

public class ClientMessageHandler {

    private Client client;
    public ClientMessageHandler(Client client) {
        this.client =client;
    }


    public void handlePingPong(PingPongMessage message){
        //TODO no client yet
    }

    public void handleConnectionMessage(ConnectionMessage message){
        //TODO no client yet
    }

    public void handleError(ErrorMessage message){
        //TODO no client yet
    }

    //Connection message handler
    public void connectNewUser(ConnectionMessage message){
        PrintAssistant.instance.printf(message.getMessage());
    }

    public void waitingPeople(ConnectionMessage message){
        PrintAssistant.instance.printf(message.getMessage());
    }

    public void username(ConnectionMessage message){
        PrintAssistant.instance.printf(message.getMessage());
        client.writeToStream(new ConnectionMessage(ConnectionType.USERNAME, client.waitToStringInputCLI()));
    }

    public void numberOfPlayer(ConnectionMessage message){
        PrintAssistant.instance.printf(message.getMessage());
        client.writeToStream(new ConnectionMessage(ConnectionType.NUM_OF_PLAYER, "", client.waitToIntegerInputCLI()));
    }

    //MainMenu message handler
    public void mainMenu(){
        ArrayList<String> texts = new ArrayList<>();
        texts.add("Main menu");
        texts.add("1)Play multiplayer");
        texts.add("2)Play single player");
        texts.add("3)Reconnect to last game");
        texts.add("4)Quit");
        PrintAssistant.instance.printfMultipleString(texts, PrintAssistant.ANSI_RED);
        client.setChooseAction(client.waitToIntegerInputCLI());
    }
}
