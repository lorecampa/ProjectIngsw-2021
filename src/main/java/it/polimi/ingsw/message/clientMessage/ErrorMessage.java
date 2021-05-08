package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;

import java.util.Optional;


public class ErrorMessage implements ClientMessage {
    private final ErrorType errorType;
    private final String customError;
    @JsonCreator
    public ErrorMessage(@JsonProperty("errorType") ErrorType errorType) {
        this.errorType = errorType;
        this.customError = null;
    }

    @JsonCreator
    public ErrorMessage(@JsonProperty("customError") String customError) {
        this.errorType = null;
        this.customError = customError;
    }


    public Optional<ErrorType> getErrorType() {
        return Optional.ofNullable(errorType);
    }

    public Optional<String> getCustomError() {
        return Optional.ofNullable(customError);
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.handleError(this);
    }

}
