package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.ResourceData;

import java.util.ArrayList;

public class StrongboxModify {
    private final ArrayList<ResourceData> resources;

    @JsonCreator
    public StrongboxModify(@JsonProperty("resources")ArrayList<ResourceData> resources) {
        this.resources = resources;
    }

    public ArrayList<ResourceData> getResources() {
        return resources;
    }
}
