package it.polimi.ingsw.message.clientMessage;

import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.CardLeaderData;

import java.util.ArrayList;

public class LeaderSetUpMessage implements ClientMessage{

    ArrayList<CardLeaderData> leaders;

    public LeaderSetUpMessage(ArrayList<CardLeaderData> leaders) {
        this.leaders = leaders;
    }

    @Override
    public void process(ClientMessageHandler handler) {

    }
}
