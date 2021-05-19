package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.server.ServerMessageHandler;

public class StrongboxModify implements ServerMessage {
    private final ResourceData resource;

    @JsonCreator
    public StrongboxModify(@JsonProperty("resource") ResourceData resource) {
        this.resource = resource;
    }

    public ResourceData getResource() {
        return resource;
    }


    @Override
    public void process(ServerMessageHandler handler) {
        handler.handleStrongboxModify(this);
    }
}
