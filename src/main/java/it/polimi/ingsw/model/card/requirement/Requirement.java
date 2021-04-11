package it.polimi.ingsw.model.card.requirement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

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
     * @return boolean - true if he can afford it, otherwise false
     */
    boolean checkRequirement(boolean discount);

    /**
     * Method attachResourceManager attach the resource manager
     * @param resourceManager of type ResourceManager is an instance of the resource manager of the player
     */
    void attachResourceManager(ResourceManager resourceManager);

    /**
     * Method attachCardManager attach the card manager
     * @param cardManager of type CardManager is an istance of the card manager of the player
     */
    void attachCardManager(CardManager cardManager);

}
