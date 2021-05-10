package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;


public class DepotUpdate implements ClientMessage{
    private final ResourceData depot;
    private final int depotIndex;
    private final boolean isNormalDepot;
    private final String username;

    @JsonCreator
    public DepotUpdate(@JsonProperty("depot") ResourceData depot,
                       @JsonProperty("depotIndex") int depotIndex,
                       @JsonProperty("isNormalDepot") boolean isNormalDepot,
                       @JsonProperty("username") String username) {
        this.depot = depot;
        this.depotIndex = depotIndex;
        this.isNormalDepot = isNormalDepot;
        this.username = username;
    }

    public ResourceData getDepot() {
        return depot;
    }

    public int getDepotIndex() {
        return depotIndex;
    }

    public boolean isNormalDepot() {
        return isNormalDepot;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("DepotUpdateHandler");
    }
}
