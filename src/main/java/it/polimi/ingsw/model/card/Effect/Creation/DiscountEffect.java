package it.polimi.ingsw.model.card.Effect.Creation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.EffectData;
import it.polimi.ingsw.client.data.EffectType;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.model.PlayerState;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Discount class represent all effects that concern a discount.
 */
public class DiscountEffect implements Effect {
    @JsonIgnore
    private ResourceManager resourceManager = null;

    private final ArrayList<Resource> discounts;

    /**
     * Construct a discount effect with a specific discount.
     * @param discounts the resources that will be discounted.
     */
    @JsonCreator
    public DiscountEffect(@JsonProperty("discounts") ArrayList<Resource> discounts) {
        this.discounts = discounts;
    }

    /**
     * Pass all the discounts to the resource manager.
     * @param playerState the state of the turn, in this case must be of type CREATION_STATE.
     */
    @Override
    public void doEffect(PlayerState playerState) {
        if (playerState == PlayerState.LEADER_MANAGE_BEFORE){
            resourceManager.addDiscount(discounts.stream()
                    .map(x->ResourceFactory.createResource(x.getType(), x.getValue()))
                    .collect(Collectors.toCollection(ArrayList::new)));
        }

    }

    /**
     * See {@link Effect#attachMarket(Market)}.
     */
    @Override
    public void attachMarket(Market market) {}

    /**
     * See {@link Effect#attachResourceManager(ResourceManager)}.
     */
    @Override
    public void attachResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    /**
     * Return the discounts of the effect.
     * @return the discounts of the effect.
     */
    public ArrayList<Resource> getDiscounts() {
        return discounts;
    }

    /**
     * See {@link Effect#toEffectData()}.
     */
    @Override
    public EffectData toEffectData() {
        String description = "Discount effect: ";
        ArrayList<ResourceData> discount = discounts.stream().map(Resource::toClient).collect(Collectors.toCollection(ArrayList::new));
        return new EffectData(EffectType.DISCOUNT,description,discount,null);
    }

}
