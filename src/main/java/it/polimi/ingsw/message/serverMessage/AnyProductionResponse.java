package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.server.ServerMessageHandler;

import java.util.ArrayList;

public class AnyProductionResponse implements ServerMessage {
    private final ArrayList<ResourceData> resources;

    @JsonCreator
    public AnyProductionResponse(@JsonProperty("resources") ArrayList<ResourceData> resources) {
        this.resources = resources;

    }

    public ArrayList<ResourceData> getResources() {
        return resources;
    }


    @Override
    public void process(ServerMessageHandler handler) {
        System.out.println("AnyProductionResponseHandler");

    }
}
