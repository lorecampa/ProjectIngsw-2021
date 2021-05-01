package it.polimi.ingsw.message.bothMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.message.ClientMessageHandler;
import it.polimi.ingsw.message.ServerMessageHandler;
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

    @Override
    public void process(ClientMessageHandler handler) {
        handler.handleConnectionMessage(this);
    }

    @Override
    public void process(ServerMessageHandler handler) {
        handler.handleConnectionMessage(this);
    }
}
