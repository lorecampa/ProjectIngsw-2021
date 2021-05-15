package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;
import java.util.ArrayList;

public class ManageResourcesRequest implements ClientMessage{
    private final ArrayList<ResourceData> resources;
    private final boolean isFromMarket;

    @JsonCreator
    public ManageResourcesRequest(@JsonProperty("resources") ArrayList<ResourceData> resources,
                                  @JsonProperty("isFromMarket") boolean isFromMarket) {
        this.resources = resources;
        this.isFromMarket = isFromMarket;
    }

    public ArrayList<ResourceData> getResources() {
        return resources;
    }

    public boolean isFromMarket() {
        return isFromMarket;
    }

    @Override
    public void process(ClientMessageHandler handler) {

    }
}
