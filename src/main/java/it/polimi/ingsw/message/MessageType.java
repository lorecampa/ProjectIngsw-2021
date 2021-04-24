package it.polimi.ingsw.message;

public enum MessageType {
    CONNECTION("Connection"),
    ERROR("Error"),
    INFO("Info");

    private final String message;

    MessageType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
