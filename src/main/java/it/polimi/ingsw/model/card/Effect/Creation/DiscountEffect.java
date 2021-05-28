package it.polimi.ingsw.model.card.Effect.Creation;

import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Discount class defines represent all effect that concern a discount
 */
public class DiscountEffect implements Effect {
    private final ArrayList<Resource> discounts;
    private ResourceManager resourceManager = null;

    /**
     * Constructor DiscountEffect creates a new DiscountEffect instance
     * @param discounts of type ArrayList - the resources that will be discounted
     */
    @JsonCreator
    public DiscountEffect(@JsonProperty("discounts") ArrayList<Resource> discounts) {
        this.discounts = discounts;
    }

    /**
     * Method doEffect is responsible to pass all the discounts to the resource manager
     * @param playerState of type State - defines the state of the turn, in this case must be of type CREATION_STATE
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
     * Used to discard the effect when a player have a leader active and decide to discard it, we remove the effect that those leades had*/
    @Override
    public void discardEffect() {
        resourceManager.removeDiscount(discounts.stream()
                .map(x->ResourceFactory.createResource(x.getType(), x.getValue()))
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    /**
     * Method attachMarket does nothing because the production effect doesn't need it
     * @param market of type Market is the instance of the market of the game
     */
    @Override
    public void attachMarket(Market market) {}

    /**
     * Method attachResourceManager attach the resource manager in order to use it
     * @param resourceManager of type ResourceManager is an instance of the resource manager
     */
    @Override
    public void attachResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public ArrayList<Resource> getDiscounts() {
        return discounts;
    }

    @Override
    public EffectData toEffectData() {
        String description = "Discount effect: ";
        ArrayList<ResourceData> discount = discounts.stream().map(Resource::toClient).collect(Collectors.toCollection(ArrayList::new));
        return new EffectData(EffectType.DISCOUNT,description,discount,null);
    }

}
