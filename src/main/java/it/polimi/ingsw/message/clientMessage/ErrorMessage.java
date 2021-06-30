package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;

public class ErrorMessage implements ClientMessage {
    private final ErrorType errorType;
    private final String customError;

    @JsonCreator
    public ErrorMessage(@JsonProperty("errorType") ErrorType errorType,
                        @JsonProperty("customError") String customError) {
        this.errorType = errorType;
        this.customError = customError;
    }

    public ErrorMessage(String customError) {
        this.errorType = null;
        this.customError = customError;
    }

    public ErrorMessage(ErrorType errorType) {
        this.errorType = errorType;
        this.customError = null;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getCustomError() {
        return customError;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.handleError(this);
    }

}
