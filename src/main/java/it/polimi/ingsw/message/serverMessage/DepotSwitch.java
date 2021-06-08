package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.server.ServerMessageHandler;

public class DepotSwitch implements ServerMessage {
    private final int from;
    private final boolean fromNormal;
    private final int to;
    private final boolean toNormal;

    @JsonCreator
    public DepotSwitch(@JsonProperty("from")int from,
                       @JsonProperty("fromNormal")boolean fromNormal,
                       @JsonProperty("to")int to,
                       @JsonProperty("toNormal")boolean toNormal) {
        this.from = from;
        this.fromNormal = fromNormal;
        this.to = to;
        this.toNormal = toNormal;
    }

    public int getFrom() {
        return from;
    }



    public int getTo() {
        return to;
    }

    public boolean isFromNormal() {
        return fromNormal;
    }

    public boolean isToNormal() {
        return toNormal;
    }

    @Override
    public void process(ServerMessageHandler handler) {
        handler.handleSwitch(this);

        handler.getVirtualClient().ifPresent(x->x.addToLog(this));
    }

    @Override
    public String toString() {
        return " - Depot Switch";
    }
}
