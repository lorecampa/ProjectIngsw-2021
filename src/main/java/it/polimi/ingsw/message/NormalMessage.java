package it.polimi.ingsw.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NormalMessage implements Message{

    MessageType messageType;

    @JsonCreator
    public NormalMessage(@JsonProperty("messageType") MessageType messageType) { this.messageType = messageType; }

    @Override
    public String print() {
        return "Normal";
    }

    @Override
    public String toString() {
        return messageType.getMessage();
    }
}
