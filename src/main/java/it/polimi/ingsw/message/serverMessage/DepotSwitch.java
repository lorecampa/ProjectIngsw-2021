package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DepotSwitch {
    private final int from;
    private final int to;

    @JsonCreator
    public DepotSwitch(@JsonProperty("from")int from,
                       @JsonProperty("to")int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
}
