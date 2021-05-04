package it.polimi.ingsw.model.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.card.requirement.Requirement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public Leader(@JsonProperty("victoryPoints") int victoryPoints,
                  @JsonProperty("requirements") ArrayList<Requirement> requirements,
                  @JsonProperty("onActivationEffects") ArrayList<Effect> onActivationEffect,
                  @JsonProperty("onCreationEffects") ArrayList<Effect> onCreationEffects) {

        super(victoryPoints, requirements, onActivationEffect, onCreationEffects);
        this.active = false;
    }

    /**
     * Method checkRequirements checks if all requirement of the card are satisfied, leader
     * discounts are not considered in the counting
     * @return boolean - true if all requirements are satisfied, otherwise false
     */
    public boolean checkRequirements(){
        for(Requirement req: requirements) {
            if (!req.checkRequirement(false)) return false;
        }
        return true;
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
     * @param status of type boolean defines the new status of the leader card
     */
    public void setActive(boolean status) {
        this.active = status;
    }



    @Override
    public String toString() {
        String x = super.toString();
        x += "Active: " + isActive() +"\n\n";
        return x;
    }
}
