package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.server.ServerMessageHandler;

public class LeaderManage implements ServerMessage{
    private final int index;
    private final boolean discard;
    private final String username;

    @JsonCreator
    public LeaderManage(@JsonProperty("index") int index,
                        @JsonProperty("discard") boolean discard,
                        @JsonProperty("username") String username) {
        this.index = index;
        this.discard = discard;
        this.username = username;
    }

    public int getIndex() {
        return index;
    }

    public boolean isDiscard() {
        return discard;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(ServerMessageHandler handler) {

    }
}
