package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;
import java.util.ArrayList;

public class DepotPositioningRequest implements ClientMessage{
    private final ArrayList<ResourceData> resources;

    @JsonCreator
    public DepotPositioningRequest(@JsonProperty("resources") ArrayList<ResourceData> resources) {
        this.resources = resources;
    }

    public ArrayList<ResourceData> getResources() {
        return resources;
    }


    @Override
    public void process(ClientMessageHandler handler) {
        handler.handleDepotPositioningRequest(this);
    }
}
