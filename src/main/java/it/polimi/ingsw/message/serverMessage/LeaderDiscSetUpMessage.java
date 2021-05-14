package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.server.ServerMessageHandler;

import java.util.ArrayList;

public class LeaderDiscSetUpMessage implements ServerMessage{

    private final ArrayList<Integer> leaderIndexes;

    @JsonCreator
    public LeaderDiscSetUpMessage(@JsonProperty("leaderIndexes") ArrayList<Integer> leaderIndexes) {
        this.leaderIndexes = leaderIndexes;
    }

    @Override
    public void process(ServerMessageHandler handler) {
        for (int leaderIndex : leaderIndexes)
            handler.handleLeaderSetUp(leaderIndex);
    }
}
