package it.polimi.ingsw.model.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.card.activationEffect.OnActivationEffect;
import it.polimi.ingsw.model.card.requirement.Requirement;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import java.util.ArrayList;


/**
 * Class Development is a class that represent all card of type development. It extends Card adding
 * all the possibility
 */
public class Development extends  Card{
    private final int level;
    private final Color color;
    private ResourceManager resourceManager = null;

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
                       @JsonProperty("onActivationEffects") ArrayList<OnActivationEffect> onActivationEffects,
                       @JsonProperty("level") int level,
                       @JsonProperty("color") Color color) {
        super(victoryPoints, requirements, onActivationEffects);
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
     * Method setResourceManager calls the setResourceManager of the father  and the then sets
     * the resource manager to himself
     * @param resourceManager of type ResourceManager is an instance of the resource manager of the player
     */
    @Override
    public void setResourceManager(ResourceManager resourceManager) {
        super.setResourceManager(resourceManager);
        this.resourceManager = resourceManager;

    }

    @Override
    public String toString() {
        String x = super.toString();
        x += "level= " + level;
        x+= "\ncolor= " + color.getDisplayName()+ "\n";
        return x;
    }
}
