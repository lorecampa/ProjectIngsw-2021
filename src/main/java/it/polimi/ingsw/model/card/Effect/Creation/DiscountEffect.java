package it.polimi.ingsw.model.card.Effect.Creation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import java.util.ArrayList;

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
     * @param turnState of type State - defines the state of the turn, in this case must be of type CREATION_STATE
     */
    @Override
    public void doEffect(TurnState turnState) {
        if (turnState == TurnState.LEADER_MANAGE_BEFORE){
            for(Resource res: discounts){
                resourceManager.addDiscount(ResourceFactory.createResource(res.getType(), res.getValue()));
            }
        }

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




    @Override
    public String toString() {
        String x = "\ndiscounts= ";
        for (Resource res: discounts){
            x+= "{"+ res.getType().getDisplayName()+", "+res.getValue()+"}  ";
        }
        return x;
    }
}
