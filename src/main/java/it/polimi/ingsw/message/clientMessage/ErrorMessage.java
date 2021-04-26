package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.message.ClientMessageHandler;


public class ErrorMessage implements ClientMessage {
    String message;

    public ErrorMessage(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


    @Override
    public void process(ClientMessageHandler handler) {
        handler.handleErrorMessage(this);
    }
}
