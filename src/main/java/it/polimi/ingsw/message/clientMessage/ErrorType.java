package it.polimi.ingsw.message.clientMessage;

public enum ErrorType {
    INVALID_MESSAGE ("Message is not valid"),
    INVALID_USERNAME("Username is not valid"),
    FAIL_RECONNECTION("Not able to reconnect"),
    FAIL_GAME_LOADING("Error in match creation");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
