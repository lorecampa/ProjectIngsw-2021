package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.server.ServerMessageHandler;

public class LeaderManage implements ServerMessage{
    private final int index;
    private final boolean discard;

    @JsonCreator
    public LeaderManage(@JsonProperty("index") int index,
                        @JsonProperty("discard") boolean discard) {
        this.index = index;
        this.discard = discard;
    }

    public int getIndex() {
        return index;
    }

    public boolean isDiscard() {
        return discard;
    }

  

    @Override
    public void process(ServerMessageHandler handler) {

    }
}
