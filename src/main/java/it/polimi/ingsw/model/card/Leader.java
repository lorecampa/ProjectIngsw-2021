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
 * Class Leader defines a class for all card of type leader. It extends Card class adding the effect of type creation.
 */
public class Leader extends Card{
    private boolean active;

    /**
     * Construct a Leader with specific attributes.
     * @param victoryPoints the leader victory points.
     * @param requirements the leader requirements.
     * @param onCreationEffects the leader effects of type creation.
     * @param onActivationEffect the leader effects of type activation.
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
     * See {@link Card#checkRequirements()}.
     */
    public void checkRequirements() throws NotEnoughRequirementException {
        for(Requirement req: requirements) {
            req.checkRequirement(false);
        }
    }

    /**
     * Return true if the card is active.
     * @return true if the card is active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set active to true.
     */
    public void setActive() {
        this.active = true;
    }

    /**
     * Return a CardLeaderData based on the card's attributes.
     * @return a CardLeaderData based on the card's attributes.
     */
    public CardLeaderData toCardLeaderData(){
        ArrayList<ResourceData> resReq = new ArrayList<>();
        ArrayList<CardDevData> cardReq = new ArrayList<>();
        for (Requirement requirement: requirements){
            if (requirement.toResourceData() != null)
                resReq.addAll(requirement.toResourceData());
            else
                cardReq.addAll(requirement.toCardDevData());
        }
        return new CardLeaderData(getId(), getVictoryPoints(),cardReq,resReq,effectToClient(), active);
    }
}
