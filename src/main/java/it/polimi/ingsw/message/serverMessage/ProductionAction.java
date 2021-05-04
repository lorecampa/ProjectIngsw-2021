package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductionAction {
    private final int slotsIndex;
    private final boolean baseProduction;
    private final boolean isLeader;

    @JsonCreator
    public ProductionAction(@JsonProperty("slotsIndex")int slotsIndex,
                            @JsonProperty("baseProduction")boolean baseProduction,
                            @JsonProperty("isLeader") boolean isLeader) {
        this.slotsIndex = slotsIndex;
        this.baseProduction = baseProduction;
        this.isLeader = isLeader;
    }

    public int getSlotsIndex() {
        return slotsIndex;
    }

    public boolean isBaseProduction() {
        return baseProduction;
    }

    public boolean isLeader() {
        return isLeader;
    }
}
