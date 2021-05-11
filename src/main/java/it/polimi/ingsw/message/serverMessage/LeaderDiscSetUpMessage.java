package it.polimi.ingsw.message.serverMessage;

import it.polimi.ingsw.server.ServerMessageHandler;

import java.util.ArrayList;

public class LeaderDiscSetUpMessage implements ServerMessage{

    ArrayList<Integer> leaderIndexes;

    public LeaderDiscSetUpMessage(ArrayList<Integer> leaderIndexes) {
        this.leaderIndexes = leaderIndexes;
    }

    @Override
    public void process(ServerMessageHandler handler) {
        for (int leaderIndex : leaderIndexes)
            handler.handleLeaderSetUp(leaderIndex);
    }
}
