package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.server.ServerMessageHandler;

import java.util.ArrayList;

public class AnyResponse implements ServerMessage{
    private final ArrayList<ResourceData> resources;

    @JsonCreator
    public AnyResponse(@JsonProperty("resources") ArrayList<ResourceData> resources) {
        this.resources = resources;

    }

    public ArrayList<ResourceData> getResources() {
        return resources;
    }


    @Override
    public void process(ServerMessageHandler handler) {
        handler.handleAnyResponse(this);
        handler.getVirtualClient().ifPresent(x->x.addToLog(this));
    }

    @Override
    public String toString() {
        return " - Any Response";
    }
}
