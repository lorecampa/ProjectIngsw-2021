package it.polimi.ingsw.client.data;

import java.util.ArrayList;

public class CardLeaderData {
    private int victoryPoint;
    private ArrayList<CardDevData> cardReq;
    private ArrayList<ResourceData> resourceReq;
    private ArrayList<String> descriptionsLeader;
    private boolean isActive;

    public CardLeaderData(int victoryPoint, ArrayList<CardDevData> cardReq, ArrayList<ResourceData> resourceReq, ArrayList<String> descriptionsLeader, boolean isActive) {
        this.victoryPoint = victoryPoint;
        this.cardReq = cardReq;
        this.resourceReq = resourceReq;
        this.descriptionsLeader = descriptionsLeader;
        this.isActive = isActive;
    }

    public int getVictoryPoint() {
        return victoryPoint;
    }

    public ArrayList<CardDevData> getCardReq() {
        return cardReq;
    }

    public ArrayList<ResourceData> getResourceReq() {
        return resourceReq;
    }

    public ArrayList<String> getDescriptionsLeader() {
        return descriptionsLeader;
    }

    public boolean isActive() {
        return isActive;
    }
}
