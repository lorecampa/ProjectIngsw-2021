package it.polimi.ingsw.model.card.activationEffect;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.exception.CantMakeProductionException;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * ProductionEffect class represent the effect of production
 */
public class ProductionEffect implements OnActivationEffect{

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
     * Method doActivationEffect checks if the player has enough resource for the production and
     * then pass all the resource that he will gain to the resource manager and it will handle those
     * putting them to the strongbox
     * @throws NegativeResourceException when the resources in resourceCost or resourceAcquired
     * contain negative values
     * @throws CantMakeProductionException when the player can't afford the production cost
     */
    @Override
    public void doActivationEffect() throws NegativeResourceException, CantMakeProductionException {
        ArrayList<Resource> resourceCostCopy = resourceCost.stream()
                .map(res -> ResourceFactory.createResource(res.getType(), res.getValue()))
                .collect(Collectors.toCollection(ArrayList::new));


        if (resourceManager.canIAfford(resourceCostCopy, false)){
            ArrayList<Resource> resourceAcquiredCopy = resourceAcquired.stream()
                    .map(res -> ResourceFactory.createResource(res.getType(), res.getValue()))
                    .collect(Collectors.toCollection(ArrayList::new));

            //TODO
            //mothod of resource manager to add resource Acquired copy

        }else{
            //trows exception that we can't afford the production
            throw new CantMakeProductionException("Can't afford resource cost production");
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
