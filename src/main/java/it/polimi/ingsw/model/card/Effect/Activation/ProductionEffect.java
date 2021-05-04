package it.polimi.ingsw.model.card.Effect.Activation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.exception.CantMakeProductionException;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.card.Effect.State;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * ProductionEffect class represent the effect of production
 */
public class ProductionEffect implements Effect {

    private final ArrayList<Resource>  resourceCost;
    private final ArrayList<Resource> resourceAcquired;
    private ResourceManager resourceManager = null;

    /**
     * Constructor ProductionEffect creates a new ProductionEffect instance
     * @param resourceCost of type ArrayList - the resources required for the production
     * @param resourceAcquired of type ArrayList - the resources that the player will gain
     */
    @JsonCreator
    public ProductionEffect(@JsonProperty("resourceCost") ArrayList<Resource> resourceCost,
                            @JsonProperty("resourceAcquired") ArrayList<Resource> resourceAcquired) {
        this.resourceCost = resourceCost;
        this.resourceAcquired = resourceAcquired;
    }


    /**
     * Method doEffect checks if the player has enough resource for the production, if it does
     * then pass all the resource that he will gain to the resource manager and it will handle those
     * putting them into the strongbox, otherwise throws CantMakeProductionException
     * @param state of type State - defines the state of the turn, in this case must be of type PRODUCTION_STATE
     * @throws CantMakeProductionException when the player can't afford the production cost
     */
    @Override
    public void doEffect(State state) throws  CantMakeProductionException {
        if (state == State.PRODUCTION_STATE){
            ArrayList<Resource> resourceCostCopy = resourceCost.stream()
                    .map(res -> ResourceFactory.createResource(res.getType(), res.getValue()))
                    .collect(Collectors.toCollection(ArrayList::new));

            if (resourceManager.canIAfford(resourceCostCopy, false)){

                resourceManager.addToResourcesToProduce(
                        resourceAcquired.stream().
                        map(x -> ResourceFactory.createResource(x.getType(), x.getValue()))
                        .collect(Collectors.toCollection(ArrayList::new))
                );


            }else{
                throw new CantMakeProductionException("Can't afford resource cost production");
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
     * @param resourceManager of type ResourceManager is an instance of the resource manager of the player
     */
    @Override
    public void attachResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @Override
    public String toString() {
        String x = "\nresourceCost= ";
        for(Resource res: resourceCost){
            x+= "{"+ res.getType().getDisplayName()+", "+res.getValue()+"}  ";
        }
        x+= "\nresourceAcquired= ";
        for(Resource res: resourceAcquired){
            x+= "{"+ res.getType().getDisplayName()+", "+res.getValue()+"}  ";
        }
        return  x;

    }
}
