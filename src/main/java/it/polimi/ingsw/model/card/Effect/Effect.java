package it.polimi.ingsw.model.card.Effect;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.exception.CantMakeProductionException;
import it.polimi.ingsw.model.card.Effect.Activation.MarbleEffect;
import it.polimi.ingsw.model.card.Effect.Activation.ProductionEffect;
import it.polimi.ingsw.model.card.Effect.Creation.DiscountEffect;
import it.polimi.ingsw.model.card.Effect.Creation.WarehouseEffect;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

/**
 * Effect class defines an interface for all kind of effects
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DiscountEffect.class, name = "DiscountEffect"),
        @JsonSubTypes.Type(value = WarehouseEffect.class, name = "WarehouseEffect"),
        @JsonSubTypes.Type(value = ProductionEffect.class, name = "ProductionEffect"),
        @JsonSubTypes.Type(value = MarbleEffect.class, name = "MarbleEffect")
})
public interface Effect {
    /**
     * Method doEffect is responsible of doing the effect itself
     * @param turnState of type State - defines the state of the turn, it controls which effect are going to be
     *              executed as mentioned below.
     *              (PRODUCTION_STATE -> ProductionEffect)
     *              (MARKET_STATE -> MarbleEffect)
     *              (CREATION_STATE -> DiscountEffect and WarehouseEffect)
     * @throws CantMakeProductionException when the player can't afford the production cost
     * (Throw only in PRODUCTION_STATE)
     */
    void doEffect(TurnState turnState) throws  CantMakeProductionException;

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
