package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.CardLeaderData;

import java.util.ArrayList;

public class LeaderActivate implements ClientMessage{
    private final ArrayList<CardLeaderData> leaders;
    private final String username;

    @JsonCreator
    public LeaderActivate(@JsonProperty("leaders") ArrayList<CardLeaderData> leaders,
                          @JsonProperty("username") String username) {
        this.leaders = leaders;
        this.username = username;
    }

    public ArrayList<CardLeaderData> getLeaders() {
        return leaders;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.activeLeader(this);
    }
}
