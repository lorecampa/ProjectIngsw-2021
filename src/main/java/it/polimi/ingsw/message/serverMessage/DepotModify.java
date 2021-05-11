package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.server.ServerMessageHandler;

import java.util.ArrayList;

public class DepotModify implements ServerMessage {
    private final int depotIndex;
    private final ArrayList<ResourceData> resources;
    private final boolean add;
    private final boolean normalDepot;


    @JsonCreator
    public DepotModify(@JsonProperty("depotIndex") int depotIndex,
                       @JsonProperty("resources")ArrayList<ResourceData> resources,
                       @JsonProperty("normalDepot") boolean normalDepot,
                       @JsonProperty("add")boolean add) {
        this.depotIndex = depotIndex;
        this.resources = resources;
        this.add = add;
        this.normalDepot = normalDepot;
    }

    public int getDepotIndex() {
        return depotIndex;
    }

    public ArrayList<ResourceData> getResources() {
        return resources;
    }

    public boolean isAdd() {
        return add;
    }

    public boolean isNormalDepot() {
        return normalDepot;
    }

    @Override
    public void process(ServerMessageHandler handler) {
        System.out.println("DepotModify Handler");
    }
}
