package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.serverMessage.ServerMessage;
import it.polimi.ingsw.server.ServerMessageHandler;

import java.util.ArrayList;
import java.util.Optional;

public class DepotModify implements ServerMessage {
    private final int depotIndex;
    private final ArrayList<ResourceData> resources;
    private final boolean isAdd;
    private final boolean isNormalDepot;


    @JsonCreator
    public DepotModify(@JsonProperty("depotIndex") int depotIndex,
                       @JsonProperty("resources")ArrayList<ResourceData> resources,
                       @JsonProperty("isNormalDepot") boolean isNormalDepot,
                       @JsonProperty("isAdd")boolean isAdd) {
        this.depotIndex = depotIndex;
        this.resources = resources;
        this.isAdd = isAdd;
        this.isNormalDepot = isNormalDepot;
    }

    public int getDepotIndex() {
        return depotIndex;
    }

    public ArrayList<ResourceData> getResources() {
        return resources;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public boolean isNormalDepot() {
        return isNormalDepot;
    }

    @Override
    public void process(ServerMessageHandler handler) {
        System.out.println("DepotModify Handler");
    }
}
