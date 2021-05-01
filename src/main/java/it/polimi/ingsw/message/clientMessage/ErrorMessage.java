package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.message.ClientMessageHandler;
import it.polimi.ingsw.message.ServerMessageHandler;


public class ErrorMessage implements ClientMessage {
    ErrorType errorType;

    @JsonCreator
    public ErrorMessage(@JsonProperty("errorType") ErrorType errorType) {
        this.errorType = errorType;
    }


    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.handleError(this);
    }

}
