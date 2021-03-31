package it.polimi.ingsw.model.card.activationEffect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @Type(value = MarbleEffect.class, name = "MarbleEffect"),
        @Type(value = ProductionEffect.class, name = "ProductionEffect")
})
public interface OnActivationEffect {
    void doActivationEffect() throws NegativeResourceException;
    void attachMarket(Market market);
    void attachResourceManager(ResourceManager resourceManager);
}
