package it.polimi.ingsw.client;


import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;
import it.polimi.ingsw.message.bothArchitectureMessage.PingPongMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;
import it.polimi.ingsw.message.clientMessage.GameSetup;
import it.polimi.ingsw.message.clientMessage.StarTurn;

import java.util.ArrayList;

public class ClientMessageHandler {

    private Client client;
    public ClientMessageHandler(Client client) {
        this.client =client;
    }

    public void handlePingPong(PingPongMessage message){
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
    }

    public void numberOfPlayer(ConnectionMessage message){
        PrintAssistant.instance.printf(message.getMessage());
    }

    public void connectInfo(ConnectionMessage message){PrintAssistant.instance.printf(message.getMessage());}

    public void reconnect(ReconnectionMessage message){
        PrintAssistant.instance.printf(message.getClientID() +" "+ message.getMatchID() + "");
        //TODO save on file matchID and clientID
        }
    //MainMenu message handler
    public void mainMenu(){
        ArrayList<String> texts = new ArrayList<>();
        texts.add("Main menu");
        texts.add("1)Play multiplayer");
        texts.add("2)Play single player");
        texts.add("3)Reconnect to last game");
        PrintAssistant.instance.printfMultipleString(texts, PrintAssistant.ANSI_RED);
    }

    //NewTurn message handler
    public void newTurn(StarTurn message){
        if(message.getUsername().equals(client.getMyName())){
            client.setGameState(ClientGameState.START_YOUR_TURN);
        }
    }

    //GameSetup message handler
    public void gameSetUp(GameSetup message){
        client.setModels(message.getUsernames());
        client.setMarketData(message.getMarket());
        client.setDeckDevData(message.getDeckDev());
    }
}
