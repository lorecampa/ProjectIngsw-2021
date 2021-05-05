package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.ResourceData;

import java.util.ArrayList;

public class WarehousePlayerUpdate {
    private final ArrayList<ResourceData> depots;
    private final ArrayList<ResourceData> strongbox;
    private final String username;

    @JsonCreator
    public WarehousePlayerUpdate(@JsonProperty("depots")ArrayList<ResourceData> depots,
                                 @JsonProperty("strongbox")ArrayList<ResourceData> strongbox,
                                 @JsonProperty("username") String username) {
        this.depots = depots;
        this.strongbox = strongbox;
        this.username = username;
    }

    public ArrayList<ResourceData> getDepots() {
        return depots;
    }

    public ArrayList<ResourceData> getStrongbox() {
        return strongbox;
    }

    public String getUsername() {
        return username;
    }
}
