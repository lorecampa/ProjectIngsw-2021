package it.polimi.ingsw.model.card.creationEffect;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

/**
 * OnCreationEffect class defines an interface for all effects that are used once
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @Type(value = DiscountEffect.class, name = "DiscountEffect"),
        @Type(value = WarehouseEffect.class, name = "WarehouseEffect")
})
public interface OnCreationEffect {
    /**
     * Method doCreationEffect is responsible of doing the effect
     */
    void doCreationEffect();

    /**
     * Method attachResourceManager attach the resource manager
     * @param resourceManager of type ResourceManager is an instance of the resource manager
     *                        of the player
     */
    void attachResourceManager(ResourceManager resourceManager);

    /**
     * Method isUsed return the status of the effect
     * @return boolean - true if the effect has been used, otherwise false
     */
    boolean isUsed();

}

