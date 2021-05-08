package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;
import java.util.ArrayList;

public class BufferUpdate implements ClientMessage{
    //i don't know if i need to send it to all players
    private final ArrayList<ResourceData> resources;

    @JsonCreator
    public BufferUpdate(@JsonProperty("resources") ArrayList<ResourceData> resources) {
        this.resources = resources;
    }


    public ArrayList<ResourceData> getResources() {
        return resources;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("BufferUpdateHandler");
    }
}
