package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.ResourceData;

public class DepotModify {
    private final int depotIndex;
    private final ResourceData resource;
    private final boolean isAdd;

    @JsonCreator
    public DepotModify(@JsonProperty("depotIndex") int depotIndex,
                       @JsonProperty("resource")ResourceData resource,
                       @JsonProperty("isAdd")boolean isAdd) {
        this.depotIndex = depotIndex;
        this.resource = resource;
        this.isAdd = isAdd;
    }

    public int getDepotIndex() {
        return depotIndex;
    }

    public ResourceData getResource() {
        return resource;
    }

    public boolean isAdd() {
        return isAdd;
    }
}
