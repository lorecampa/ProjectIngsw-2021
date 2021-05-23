package it.polimi.ingsw.model.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.CardDevData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.NotEnoughRequirementException;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.card.requirement.Requirement;
import java.util.ArrayList;


/**
 * Class Development is a class that represent all card of type development. It extends Card adding
 * all the possibility
 */
public class Development extends  Card{
    private final int level;
    private final Color color;


    /**
     * Constructor Development creates a new Development instance
     * @param victoryPoints of type int - the development card victory points
     * @param requirements of type ArrayList - the development card requirements
     * @param onActivationEffects of type ArrayList - the development card effects of type activation
     * @param level of type int - the development card level
     * @param color of type Color - the development card color
     */
    @JsonCreator
    public Development(@JsonProperty("id") int id,
                       @JsonProperty("victoryPoints") int victoryPoints,
                       @JsonProperty("requirements") ArrayList<Requirement> requirements,
                       @JsonProperty("onActivationEffects") ArrayList<Effect> onActivationEffects,
                       @JsonProperty("onCreationEffects") ArrayList<Effect> onCreationEffects,
                       @JsonProperty("level") int level,
                       @JsonProperty("color") Color color) {
        super(id, victoryPoints, requirements, onActivationEffects, onCreationEffects);
        this.level = level;
        this.color = color;
    }

    public int getLevel() {
        return level;
    }

    public Color getColor() {
        return color;
    }

    public CardDevData toCardDevData(){
        ArrayList<ResourceData> resReq = new ArrayList<>();
        for (Requirement requirement: requirements){
            if (requirement.toResourceData() != null)
                resReq.addAll(requirement.toResourceData());
        }
        return new CardDevData(level,getVictoryPoints(),color.toColorData(), resReq, effectToClient());
    }

    /**
     * Method checkRequirements checks if all requirement of the card are satisfied, leaders discounts
     * are considered in the counting
     */
    public void checkRequirements() throws NotEnoughRequirementException {
        for(Requirement req: requirements){
            req.checkRequirement(true);
        }

    }

    @Override
    public String toString() {
        String x = super.toString();
        x += "level= " + level;
        x+= "\ncolor= " + color.getDisplayName()+ "\n";
        return x;
    }
}
