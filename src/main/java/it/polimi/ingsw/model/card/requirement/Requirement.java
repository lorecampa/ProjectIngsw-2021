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
 * Requirement define an interface for all type of requirement a card could have in order to be
 * purchased(development card) or activated(leader card).
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")

@JsonSubTypes({
        @Type(value = CardReq.class, name = "CardReq"),
        @Type(value = ResourceReq.class, name = "ResourceReq")
})
public interface Requirement {

    /**
     * Control if the player can afford it.
     * @param discount true if you want to consider the leader discount effect.
     * @throws NotEnoughRequirementException if the player doesn't have enough card or resources to satisfy
     * the requirement.
     */
    void checkRequirement(boolean discount) throws NotEnoughRequirementException;

    /**
     * Attach the resource manager of the player.
     * @param resourceManager the resource manager of the player.
     */
    void attachResourceManager(ResourceManager resourceManager);

    /**
     * Attach the card manager of the player.
     * @param cardManager the card manager of the player.
     */
    void attachCardManager(CardManager cardManager);

    /**
     * Return an ArrayList of ResourceData based on the resources requirement.
     * @return an ArrayList of ResourceData based on the resources requirement.
     */
    ArrayList<ResourceData> toResourceData();

    /**
     * Return an ArrayList of CardDevData based on the cards requirement.
     * @return an ArrayList of CardDevData based on the cards requirement.
     */
    ArrayList<CardDevData> toCardDevData();
}
