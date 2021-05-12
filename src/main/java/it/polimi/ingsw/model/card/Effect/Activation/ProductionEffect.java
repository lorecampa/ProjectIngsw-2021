package it.polimi.ingsw.model.card.Effect.Activation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.EffectData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.NotEnoughRequirementException;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.controller.TurnState;
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
     * @param turnState of type State - defines the state of the turn, in this case must be of type PRODUCTION_STATE
     */
    @Override
    public void doEffect(TurnState turnState) throws NotEnoughRequirementException {
        if (turnState == TurnState.PRODUCTION_ACTION){
            ArrayList<Resource> resourceCostCopy = resourceCost.stream()
                    .map(res -> ResourceFactory.createResource(res.getType(), res.getValue()))
                    .collect(Collectors.toCollection(ArrayList::new));

            resourceManager.canIAfford(resourceCostCopy, false);

            resourceManager.addToResourcesToProduce(resourceAcquired.stream()
                    .map(x -> ResourceFactory.createResource(x.getType(), x.getValue()))
                    .collect(Collectors.toCollection(ArrayList::new)), true, true);
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
    public EffectData toEffectData() {
        String description = "Production effect: ";
        ArrayList<ResourceData> productionCost = resourceCost.stream().map(Resource::toClient).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<ResourceData> productionEarn = resourceAcquired.stream().map(Resource::toClient).collect(Collectors.toCollection(ArrayList::new));
        return new EffectData(description,productionCost, productionEarn);
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
