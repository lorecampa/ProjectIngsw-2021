package it.polimi.ingsw.client.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class CardLeaderData {
    private int victoryPoint;
    private ArrayList<CardDevData> cardReq;
    private ArrayList<ResourceData> resourceReq;

    private ArrayList<EffectData> effects;

    private ArrayList<String> descriptionsLeader;
    private boolean active;


    public CardLeaderData(@JsonProperty("victoryPoint")int victoryPoint,
                          @JsonProperty("cardReq")ArrayList<CardDevData> cardReq,
                          @JsonProperty("resourceReq")ArrayList<ResourceData> resourceReq,
                          @JsonProperty("descriptionsLeader")ArrayList<String> descriptionsLeader,
                          @JsonProperty("active")boolean active) {
        this.victoryPoint = victoryPoint;
        this.cardReq = cardReq;
        this.resourceReq = resourceReq;
        this.descriptionsLeader = descriptionsLeader;
        this.active = active;
    }

    @JsonCreator(mode= JsonCreator.Mode.PROPERTIES)
    public CardLeaderData(@JsonProperty("victoryPoint")int victoryPoint,
                          @JsonProperty("cardReq")ArrayList<CardDevData> cardReq,
                          @JsonProperty("resourceReq")ArrayList<ResourceData> resourceReq,
                          @JsonProperty("effects")ArrayList<EffectData> effects,
                          @JsonProperty("descriptionsLeader")ArrayList<String> descriptionsLeader,
                          @JsonProperty("active")boolean active) {
        this.victoryPoint = victoryPoint;
        this.cardReq = cardReq;
        this.resourceReq = resourceReq;
        this.effects = effects;
        this.descriptionsLeader=null;
        this.active = false;
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

    public ArrayList<EffectData> getEffects() {
        return effects;
    }

    public ArrayList<String> getDescriptionsLeader() {
        return descriptionsLeader;
    }

    public boolean isActive() {
        return active;
    }
}
