package it.polimi.ingsw.message.bothMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.server.ServerMessageHandler;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.serverMessage.ServerMessage;

public class ConnectionMessage implements ClientMessage, ServerMessage {
    private ConnectionType type;
    private String message;
    private int num;


    @JsonCreator
    public ConnectionMessage(@JsonProperty("type") ConnectionType type,
                             @JsonProperty("message") String message,
                             @JsonProperty("num") int num) {
        this.type = type;
        this.message = message;
        this.num = num;
    }

    public ConnectionMessage(@JsonProperty("type") ConnectionType type,
                             @JsonProperty("message") String message) {
        this.type = type;
        this.message = message;
        this.num = -1;
    }

    public String getMessage() {
        return message;
    }
    public String getType(){
        return type.toString();
    }
    public int getNum(){return  num;}

    @Override
    public void process(ClientMessageHandler handler) {
        switch(type){
            case CONNECT:
                handler.connectNewUser(this);
                break;
            case WAIT_PLAYERS:
                handler.waitingPeople(this);
                break;
            case USERNAME:
                handler.username(this);
                break;
            case NUM_OF_PLAYER:
                handler.numberOfPlayer(this);
                break;
            default:
        }
        //handler.handleConnectionMessage(this);
    }

    @Override
    public void process(ServerMessageHandler handler) {
        switch (type){
            case CONNECT:
                handler.handleFirstContact(this);
                break;
            case NUM_OF_PLAYER:
                handler.handleMatchCreation(this);
                break;
            case USERNAME:
                handler.handleUsernameInput(this);
                break;
        }
        //handler.handleConnectionMessage(this);
    }
}
