package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;
import java.util.ArrayList;

public class BufferUpdate implements ClientMessage{
    private final ArrayList<ResourceData> bufferUpdated;
    private final boolean fromMarket;
    private final boolean haveToPrintText;
    @JsonCreator
    public BufferUpdate(@JsonProperty("bufferUpdated") ArrayList<ResourceData> bufferUpdated,
                        @JsonProperty("fromMarket") boolean fromMarket,
                        @JsonProperty("haveToPrintText") boolean haveToPrintText) {
        this.bufferUpdated = bufferUpdated;
        this.fromMarket = fromMarket;
        this.haveToPrintText = haveToPrintText;
    }

    public BufferUpdate(ArrayList<ResourceData> bufferUpdated, boolean fromMarket) {
        this.bufferUpdated = bufferUpdated;
        this.fromMarket = fromMarket;
        this.haveToPrintText = true;
    }

    public BufferUpdate(ArrayList<ResourceData> bufferUpdated) {
        this.bufferUpdated = bufferUpdated;
        this.haveToPrintText = false;
        this.fromMarket = false;
    }

    public ArrayList<ResourceData> getBufferUpdated() {
        return bufferUpdated;
    }

    public boolean isFromMarket() {
        return fromMarket;
    }

    public boolean isHaveToPrintText() {
        return haveToPrintText;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("BufferUpdateHandler");
    }
}
