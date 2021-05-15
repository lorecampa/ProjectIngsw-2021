package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;
import java.util.ArrayList;

public class BufferUpdate implements ClientMessage{
    private final ArrayList<ResourceData> bufferUpdated;

    @JsonCreator
    public BufferUpdate(@JsonProperty("bufferUpdated") ArrayList<ResourceData> bufferUpdated) {
        this.bufferUpdated = bufferUpdated;
    }

    public ArrayList<ResourceData> getBufferUpdated() {
        return bufferUpdated;
    }


    @Override
    public void process(ClientMessageHandler handler) {
        handler.bufferUpdate(this);
    }
}
