package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.CLIMessageHandler;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;
import java.util.ArrayList;

public class ManageResourcesRequest implements ClientMessage{
    private final ArrayList<ResourceData> resources;
    private final boolean fromMarket;

    @JsonCreator
    public ManageResourcesRequest(@JsonProperty("resources") ArrayList<ResourceData> resources,
                                  @JsonProperty("fromMarket") boolean fromMarket) {
        this.resources = resources;
        this.fromMarket = fromMarket;
    }

    public ArrayList<ResourceData> getResources() {
        return resources;
    }

    public boolean isFromMarket() {
        return fromMarket;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.manageResourceRequest(this);
    }
}
