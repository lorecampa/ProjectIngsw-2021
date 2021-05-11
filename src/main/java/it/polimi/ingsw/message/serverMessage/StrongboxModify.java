package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.server.ServerMessageHandler;

import java.util.ArrayList;

public class StrongboxModify implements ServerMessage {
    private final ArrayList<ResourceData> resources;
    private final boolean add;


    @JsonCreator
    public StrongboxModify(@JsonProperty("resources") ArrayList<ResourceData> resources,
                           @JsonProperty("add") boolean add) {
        this.resources = resources;
        this.add = add;
    }

    public ArrayList<ResourceData> getResources() {
        return resources;
    }

    public boolean isAdd() {
        return add;
    }

    @Override
    public void process(ServerMessageHandler handler) {
        System.out.println("StrongboxModifyHandler");
    }
}
