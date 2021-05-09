package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.serverMessage.ServerMessage;
import it.polimi.ingsw.server.ServerMessageHandler;

import java.util.ArrayList;
import java.util.Optional;

public class StrongboxModify implements ServerMessage {
    private final ArrayList<ResourceData> resources;
    private final boolean isAdd;


    @JsonCreator
    public StrongboxModify(@JsonProperty("resources") ArrayList<ResourceData> resources,
                           @JsonProperty("isAdd") boolean isAdd) {
        this.resources = resources;
        this.isAdd = isAdd;
    }

    public ArrayList<ResourceData> getResources() {
        return resources;
    }

    public boolean isAdd() {
        return isAdd;
    }

    @Override
    public void process(ServerMessageHandler handler) {
        System.out.println("StrongboxModifyHandler");
    }
}
