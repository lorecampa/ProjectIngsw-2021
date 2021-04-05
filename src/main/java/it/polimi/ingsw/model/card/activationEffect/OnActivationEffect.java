package it.polimi.ingsw.model.card.activationEffect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.exception.CantMakeProduction;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

/**
 * OnActivationEffect class defines an interface for all effects with multiple activation
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @Type(value = MarbleEffect.class, name = "MarbleEffect"),
        @Type(value = ProductionEffect.class, name = "ProductionEffect")
})
public interface OnActivationEffect {
    /**
     * Method doActivationEffect is responsible of doing the effect
     * @throws NegativeResourceException when the resources contain negative values
     * @throws CantMakeProduction when the player can't afford the production cost
     */
    void doActivationEffect() throws NegativeResourceException, CantMakeProduction;

    /**
     * Method attachMarket attach the market
     * @param market of type Market is the instance of the market of the game
     */
    void attachMarket(Market market);

    /**
     * Method attachResourceManager attach the resource manager
     * @param resourceManager of type ResourceManager is an instance of the resource manager of the player
     */
    void attachResourceManager(ResourceManager resourceManager);
}
