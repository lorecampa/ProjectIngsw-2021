package it.polimi.ingsw.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NormalMessage implements Message{
    String message;

    @JsonCreator
    public NormalMessage(@JsonProperty("message") String message) { this.message = message; }

    @Override
    public String print() {
        return "Normal";
    }

    @Override
    public String toString() {
        return message;
    }
}
