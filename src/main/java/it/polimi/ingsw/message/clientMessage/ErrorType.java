package it.polimi.ingsw.message.clientMessage;

public enum ErrorType {
    INVALID_MESSAGE ("Message is not valid"),
    INVALID_USERNAME("Username is not valid"),
    NOT_YOUR_TURN("It is not your turn, please wait!"),
    FAIL_RECONNECTION("Not able to reconnect"),
    FAIL_GAME_LOADING("Error in match creation"),
    //GAME ERROR
    NOT_ENOUGH_REQUIREMENT("You don't have enough requirements to buy this card");


    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
