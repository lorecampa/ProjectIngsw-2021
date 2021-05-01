package it.polimi.ingsw.client;


import it.polimi.ingsw.message.bothMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothMessage.ConnectionType;
import it.polimi.ingsw.message.bothMessage.PingPongMessage;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;

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
        System.out.println(message.getMessage());
    }

    public void waitingPeople(ConnectionMessage message){
        System.out.println(message.getMessage());
    }

    public void username(ConnectionMessage message){
        System.out.println(message.getMessage());
        client.writeToStream(new ConnectionMessage(ConnectionType.USERNAME, client.waitToStringInputCLI()));
    }

    public void numberOfPlayer(ConnectionMessage message){
        System.out.println(message.getMessage());
        client.writeToStream(new ConnectionMessage(ConnectionType.NUM_OF_PLAYER, "", client.waitToIntegerInputCLI()));
    }

}
