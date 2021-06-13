package it.polimi.ingsw.model.card.requirement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * CardReq represent a class for all kind of card requirement.
 */
public class CardReq implements Requirement {
    @JsonIgnore
    CardManager cardManager = null;

    Color color;
    int level;
    int numRequired;

    /**
     * Construct a Card Requirement of a specific card.
     * @param color the required color of the card.
     * @param level the requirement level of the card.
     * @param numRequired the amount of card with that color and level.
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
     * Control if the player has enough cards with a specific color and level.
     * @param discount doesn't matter if it is true or false.
     * @throws NotEnoughRequirementException if the player doesn't have enough card or resources to satisfy
     * the requirement.
     */
    @Override
    public void checkRequirement(boolean discount) throws NotEnoughRequirementException {
        cardManager.doIHaveDev(numRequired, color, level);
    }


    /**
     * See {@link Requirement#attachResourceManager(ResourceManager)}.
     */
    @Override
    public void attachResourceManager(ResourceManager resourceManager) {}

    /**
     * See {@link Requirement#attachCardManager(CardManager)}.
     */
    @Override
    public void attachCardManager(CardManager cardManager) {
        this.cardManager = cardManager;
    }

    /**
     * See {@link Requirement#toResourceData()}.
     */
    @Override
    public ArrayList<ResourceData> toResourceData() {
        return null;
    }

    /**
     * See {@link Requirement#toCardDevData()}.
     */
    @Override
    public ArrayList<CardDevData> toCardDevData() {
        return new ArrayList<>(Collections.nCopies(numRequired,new CardDevData(level,color.toColorData())));
    }

}
