package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.server.ServerMessageHandler;

public class ProductionAction implements ServerMessage{
    private final int slotsIndex;
    private final boolean leader;

    @JsonCreator
    public ProductionAction(@JsonProperty("slotsIndex")int slotsIndex,
                            @JsonProperty("leader") boolean leader) {
        this.slotsIndex = slotsIndex;
        this.leader = leader;
    }

    public int getSlotsIndex() {
        return slotsIndex;
    }

    public boolean isLeader() {
        return leader;
    }

    @Override
    public void process(ServerMessageHandler handler) {
        handler.handleProduction(this);
    }
}
