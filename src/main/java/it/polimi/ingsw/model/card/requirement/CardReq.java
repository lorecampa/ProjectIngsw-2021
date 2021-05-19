package it.polimi.ingsw.model.card.requirement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.CardDevData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.NotEnoughRequirementException;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class CardReq represent a class for all kind of card requirement
 */
public class CardReq implements Requirement {
    Color color;
    int level;
    int numRequired;
    CardManager cardManager = null;

    /**
     * Constructor CardReq creates a new CardReq instance
     * @param color of type Color - the requirement type of color
     * @param level of type int - the requirement level of that color card
     * @param numRequired of type int - the amount of card with that color and level
     */
    @JsonCreator
    public CardReq(@JsonProperty("color") Color color,
                   @JsonProperty("level") int level,
                   @JsonProperty("numRequired") int numRequired) {
        this.color = color;
        this.level = level;
        this.numRequired = numRequired;
    }

    /**
     * Method checkRequirement control if the player has enough cards with a specific color
     * @param discount of type boolean - doesn't matter if it is true or false
     */
    @Override
    public void checkRequirement(boolean discount) throws NotEnoughRequirementException {
        cardManager.doIHaveDev(numRequired, color, level);
    }


    /**
     * Method attachResourceManager attach the resource manager
     * @param resourceManager of type ResourceManager is an instance of the resource manager of the player
     */
    @Override
    public void attachResourceManager(ResourceManager resourceManager) {}

    /**
     * Method attachCardManager attach the card manager
     * @param cardManager of type CardManager is an instance of the card manager of the player
     */
    @Override
    public void attachCardManager(CardManager cardManager) {
        this.cardManager = cardManager;
    }

    @Override
    public ArrayList<ResourceData> toResourceData() {
        return null;
    }

    @Override
    public ArrayList<CardDevData> toCardDevData() {
        return new ArrayList<>(Collections.nCopies(numRequired,new CardDevData(level,color.toColorData())));
    }

    @Override
    public String toString() {
        return "cardReq= " +
                "{" + color+
                ", level: " + level+
                ", numRequired: " + numRequired+"}";
    }
}
