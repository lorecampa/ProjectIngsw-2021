package it.polimi.ingsw.client.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ModelData {
    private final String username;
    private final ArrayList<FaithTrackData> faithTrack;
    private final int currentPosOnFaithTrack;
    private final ArrayList<ResourceData> standardDepot;
    private final ArrayList<ResourceData> leaderDepot;
    private final ArrayList<Integer> maxStoreLeaderDepot;
    private final ArrayList<ResourceData> strongbox;
    private final ArrayList<ArrayList<CardDevData>> cardSlots;
    private final ArrayList<CardLeaderData> leaders;

    @JsonCreator
    public ModelData(@JsonProperty("username") String username,@JsonProperty("faithTrack") ArrayList<FaithTrackData> faithTrack,
                     @JsonProperty("currentPosOnFaithTrack")int currentPosOnFaithTrack, @JsonProperty("standardDepot") ArrayList<ResourceData> standardDepot,
                     @JsonProperty("leaderDepot")ArrayList<ResourceData> leaderDepot, @JsonProperty("maxStoreLeaderDepot")ArrayList<Integer> maxStoreLeaderDepot,
                     @JsonProperty("strongbox")ArrayList<ResourceData> strongbox, @JsonProperty("cardSlots")ArrayList<ArrayList<CardDevData>> cardSlots,
                     @JsonProperty("leaders")ArrayList<CardLeaderData> leaders) {
        this.username = username;
        this.faithTrack = faithTrack;
        this.currentPosOnFaithTrack = currentPosOnFaithTrack;
        this.standardDepot = standardDepot;
        this.leaderDepot = leaderDepot;
        this.maxStoreLeaderDepot = maxStoreLeaderDepot;
        this.strongbox = strongbox;
        this.cardSlots = cardSlots;
        this.leaders = leaders;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<FaithTrackData> getFaithTrack() {
        return faithTrack;
    }

    public int getCurrentPosOnFaithTrack() {
        return currentPosOnFaithTrack;
    }

    public ArrayList<ResourceData> getStandardDepot() {
        return standardDepot;
    }

    public ArrayList<ResourceData> getLeaderDepot() {
        return leaderDepot;
    }

    public ArrayList<Integer> getMaxStoreLeaderDepot() {
        return maxStoreLeaderDepot;
    }

    public ArrayList<ResourceData> getStrongbox() {
        return strongbox;
    }

    public ArrayList<ArrayList<CardDevData>> getCardSlots() {
        return cardSlots;
    }

    public ArrayList<CardLeaderData> getLeaders() {
        return leaders;
    }
}
