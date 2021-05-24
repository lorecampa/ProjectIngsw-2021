package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;

public class LeaderDiscard implements ClientMessage{
    private final int leaderIndex;
    private final String username;

    @JsonCreator
    public LeaderDiscard(@JsonProperty("leaderIndex") int leaderIndex,
                         @JsonProperty("username") String username) {
        this.leaderIndex = leaderIndex;
        this.username = username;
    }

    public int getLeaderIndex() {
        return leaderIndex;
    }


    public String getUsername() {
        return username;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.leaderDiscardUpdate(this);
    }
}
