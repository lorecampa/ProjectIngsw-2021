package it.polimi.ingsw.message;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CommandMessage implements Message{

    MessageType messageType;
    int x;
    int y;

    @JsonCreator
    public CommandMessage(@JsonProperty("messageType") MessageType messageType,
                          @JsonProperty("x") int x,
                          @JsonProperty("y") int y) {
        this.messageType = messageType;
        this.x = x;
        this.y = y;
    }

    @Override
    public String print() {
        return "Command";
    }

    @Override
    public String toString() {
        return "CommandMessage{" +
                "messageType=" + messageType +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
