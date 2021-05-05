package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.ResourceData;

import java.util.ArrayList;

public class ResourceManagement {
    private final ArrayList<ResourceData> resources;
    private final boolean isDepotInsertion;

    @JsonCreator
    public ResourceManagement(@JsonProperty("resources")ArrayList<ResourceData> resources,
                              @JsonProperty("isDepotInsertion")boolean isDepotInsertion) {
        this.resources = resources;
        this.isDepotInsertion = isDepotInsertion;
    }

    public ArrayList<ResourceData> getResources() {
        return resources;
    }

    public boolean isDepotInsertion() {
        return isDepotInsertion;
    }
}
