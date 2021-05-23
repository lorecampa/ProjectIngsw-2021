package it.polimi.ingsw.model.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.CardDevData;
import it.polimi.ingsw.client.data.CardLeaderData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.NotEnoughRequirementException;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.card.requirement.Requirement;
import java.util.ArrayList;

/**
 * Class Leader defines a class for all card of type leader. It extends Card class adding the
 * effect of type creation
 */
public class Leader extends Card{
    private boolean active;
    /**
     * Constructor Leader creates a new Leader instance
     * @param victoryPoints of type int - the leader victory points
     * @param requirements of type ArrayList - the leader requirements
     * @param onCreationEffects of type ArrayList - the leader effects of type creation
     * @param onActivationEffect of type ArrayList - the leader effects of type activation
     */
    @JsonCreator
    public Leader(@JsonProperty("id") int id,
                  @JsonProperty("victoryPoints") int victoryPoints,
                  @JsonProperty("requirements") ArrayList<Requirement> requirements,
                  @JsonProperty("onActivationEffects") ArrayList<Effect> onActivationEffect,
                  @JsonProperty("onCreationEffects") ArrayList<Effect> onCreationEffects) {

        super(id,victoryPoints, requirements, onActivationEffect, onCreationEffects);
        this.active = false;
    }

    /**
     * Method checkRequirements checks if all requirement of the card are satisfied, leader
     * discounts are not considered in the counting
     */
    public void checkRequirements() throws NotEnoughRequirementException {
        for(Requirement req: requirements) {
            req.checkRequirement(false);
        }
    }

    /**
     * Method isActive is a getter method for knowing is the leader card has been already activated
     * by the player that owns it
     * @return boolean - true if leader card is active, otherwise false
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Method setActive is a setter method  for setting the attribute active to true when the player
     * decide to activate the leader card during the game
     */
    public void setActive() {
        this.active = true;
    }

    public CardLeaderData toCardLeaderData(){
        ArrayList<ResourceData> resReq = new ArrayList<>();
        ArrayList<CardDevData> cardReq = new ArrayList<>();
        for (Requirement requirement: requirements){
            if (requirement.toResourceData() != null)
                resReq.addAll(requirement.toResourceData());
            else
                cardReq.addAll(requirement.toCardDevData());
        }
        return new CardLeaderData(getVictoryPoints(),cardReq,resReq,effectToClient(), active);
    }

}
