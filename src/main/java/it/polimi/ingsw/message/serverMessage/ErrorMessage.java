package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.message.ClientMessageHandler;
import it.polimi.ingsw.message.ServerMessageHandler;
import it.polimi.ingsw.message.clientMessage.ClientMessage;


public class ErrorMessage implements ServerMessage {
    String message;

    public ErrorMessage(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


    @Override
    public void process(ServerMessageHandler handler) {
        handler.handleErrorMessage(this);
    }
}
