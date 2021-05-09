package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;

public class LeaderStatusUpdate implements ClientMessage{
    private final int leaderIndex;
    private final boolean discard;
    private final String username;

    @JsonCreator
    public LeaderStatusUpdate(@JsonProperty("leaderIndex") int leaderIndex,
                              @JsonProperty("discard") boolean discard,
                              @JsonProperty("username") String username) {
        this.leaderIndex = leaderIndex;
        this.discard = discard;
        this.username = username;
    }

    public int getLeaderIndex() {
        return leaderIndex;
    }

    public boolean isDiscard() {
        return discard;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("LeaderStateUpdate Handler");
    }
}
