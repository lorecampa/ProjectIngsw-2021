package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.server.ServerMessageHandler;

public class DepotModify implements ServerMessage {
    private final int depotIndex;
    private final ResourceData resource;
    private final boolean normalDepot;


    @JsonCreator
    public DepotModify(@JsonProperty("depotIndex") int depotIndex,
                       @JsonProperty("resource") ResourceData resource,
                       @JsonProperty("normalDepot") boolean normalDepot) {
        this.depotIndex = depotIndex;
        this.resource = resource;
        this.normalDepot = normalDepot;
    }

    public int getDepotIndex() {
        return depotIndex;
    }

    public ResourceData getResource() {
        return resource;
    }


    public boolean isNormalDepot() {
        return normalDepot;
    }

    @Override
    public void process(ServerMessageHandler handler) {
        handler.handleDepotModify(this);
    }
}
