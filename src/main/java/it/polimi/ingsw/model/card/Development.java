package it.polimi.ingsw.model.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    public Development(@JsonProperty("victoryPoints") int victoryPoints,
                       @JsonProperty("requirements") ArrayList<Requirement> requirements,
                       @JsonProperty("onActivationEffects") ArrayList<Effect> onActivationEffects,
                       @JsonProperty("onCreationEffects") ArrayList<Effect> onCreationEffects,
                       @JsonProperty("level") int level,
                       @JsonProperty("color") Color color) {
        super(victoryPoints, requirements, onActivationEffects, onCreationEffects);
        this.level = level;
        this.color = color;
    }

    public int getLevel() {
        return level;
    }

    public Color getColor() {
        return color;
    }



    /**
     * Method checkRequirements checks if all requirement of the card are satisfied, leaders discounts
     * are considered in the counting
     * @return boolean - true if all requirements are satisfied, otherwise false
     */
    public boolean checkRequirements(){
        for(Requirement req: requirements) {
            if (!req.checkRequirement(true)) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String x = super.toString();
        x += "level= " + level;
        x+= "\ncolor= " + color.getDisplayName()+ "\n";
        return x;
    }
}
