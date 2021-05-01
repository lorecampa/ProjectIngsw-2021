package it.polimi.ingsw.message.clientMessage;

public enum ErrorType {
    INVALID_MESSAGE ("Message is not valid"),
    INVALID_USERNAME("Username is not valid");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
