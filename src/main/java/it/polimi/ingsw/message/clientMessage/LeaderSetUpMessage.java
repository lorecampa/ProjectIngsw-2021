package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.CardLeaderData;

import java.util.ArrayList;

public class LeaderSetUpMessage implements ClientMessage{
    private final ArrayList<CardLeaderData> leaders;

    @JsonCreator
    public LeaderSetUpMessage(@JsonProperty("leaders")ArrayList<CardLeaderData> leaders) {
        this.leaders = leaders;
    }

    public ArrayList<CardLeaderData> getLeaders() {
        return leaders;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.leaderSetUp(this);
    }
}
