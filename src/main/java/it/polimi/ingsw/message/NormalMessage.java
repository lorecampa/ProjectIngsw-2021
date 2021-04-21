package it.polimi.ingsw.message;

public class NormalMessage implements Message{
    String message;

    public NormalMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
