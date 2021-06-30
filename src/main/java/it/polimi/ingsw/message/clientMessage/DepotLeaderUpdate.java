package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;

import java.util.ArrayList;

public class DepotLeaderUpdate implements ClientMessage {

    private final ArrayList<ResourceData> depots;
    private final boolean discard;
    private final String username;

    @JsonCreator
    public DepotLeaderUpdate(@JsonProperty("depot") ArrayList<ResourceData> depots,
                             @JsonProperty("discard") boolean discard,
                             @JsonProperty("username") String username ) {
        this.depots = depots;
        this.discard = discard;
        this.username=username;
    }

    public ArrayList<ResourceData> getDepots() {
        return depots;
    }

    public boolean isDiscard() {
        return discard;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.depotLeaderUpdate(this);
    }
}
