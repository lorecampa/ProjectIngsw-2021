package it.polimi.ingsw.model.card.requirement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.client.data.CardDevData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.NotEnoughRequirementException;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

import java.util.ArrayList;

/**
 * Class Requirement define an interface for all type of requirement a card could have in order to be
 * purchased(development card) or activated(leader card)
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")

@JsonSubTypes({
        @Type(value = CardReq.class, name = "CardReq"),
        @Type(value = ResourceReq.class, name = "ResourceReq")
})
public interface Requirement {

    /**
     * Method checkRequirement control if the player can afford it
     * @param discount of type boolean - if you want to consider the leader discount effect then it
     *                 must be true, otherwise false
     * @return boolean - true if he can afford it, otherwise false
     */
    void checkRequirement(boolean discount) throws NotEnoughRequirementException;

    /**
     * Method attachResourceManager attach the resource manager
     * @param resourceManager of type ResourceManager is an instance of the resource manager of the player
     */
    void attachResourceManager(ResourceManager resourceManager);

    /**
     * Method attachCardManager attach the card manager
     * @param cardManager of type CardManager is an instance of the card manager of the player
     */
    void attachCardManager(CardManager cardManager);

    ArrayList<ResourceData> toResourceData();

    ArrayList<CardDevData> toCardDevData();
}
