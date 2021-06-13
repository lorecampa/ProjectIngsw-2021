package it.polimi.ingsw.model.card.Effect;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.client.data.EffectData;
import it.polimi.ingsw.model.PlayerState;
import it.polimi.ingsw.exception.NotEnoughRequirementException;
import it.polimi.ingsw.model.card.Effect.Activation.MarbleEffect;
import it.polimi.ingsw.model.card.Effect.Activation.ProductionEffect;
import it.polimi.ingsw.model.card.Effect.Creation.DiscountEffect;
import it.polimi.ingsw.model.card.Effect.Creation.WarehouseEffect;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

/**
 * Effect class defines an interface for all kind of effects.
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
     * Perform the effect's action.
     * @param playerState defines the state of the turn, it controls which effect are going to be
     *              executed as mentioned below.
     *              (PRODUCTION_STATE -> ProductionEffect)
     *              (MARKET_STATE -> MarbleEffect)
     *              (CREATION_STATE -> DiscountEffect and WarehouseEffect)
     * @throws NotEnoughRequirementException if the player doesn't have enough card or resources to satisfy
     * all requirements of the effect.
     */
    void doEffect(PlayerState playerState) throws NotEnoughRequirementException;

    /**
     * Attach the market.
     * @param market the market of the game.
     */
    void attachMarket(Market market);

    /**
     * Attach the resource manager.
     * @param resourceManager the resource manager of the player.
     */
    void attachResourceManager(ResourceManager resourceManager);

    /**
     * Return an EffectData based on the effect type and attributes.
     * @return an EffectData based on the effect type and attributes.
     */
    EffectData toEffectData();
}
