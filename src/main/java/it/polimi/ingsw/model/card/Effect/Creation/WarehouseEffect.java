package it.polimi.ingsw.model.card.Effect.Creation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.card.Effect.State;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.Depot;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import java.util.ArrayList;

/**
 * Class WarehouseEffect defines a class for all effect that will modify the warehouse structure
 */
public class WarehouseEffect  implements Effect {
    private final ArrayList<Resource> depots;
    private ResourceManager resourceManager = null;



    /**
     * Constructor WarehouseEffect creates a new WarehouseEffect instance
     * @param depots of type ArrayList - the amount of resources that will be transformed in depot
     */
    @JsonCreator
    public WarehouseEffect(@JsonProperty("depots") ArrayList<Resource> depots) {
        this.depots = depots;
    }

    /**
     * Method doEffect creates a new locked depot (lockDepot = true)  for all the resources in depots
     * @param state of type State - defines the state of the turn
     */
    @Override
    public void doEffect(State state) {
        if (state == State.CREATION_STATE){
            for (Resource depot: depots){
                Resource res = ResourceFactory.createResource(depot.getType(), 0);
                resourceManager.addLeaderDepot(new Depot(res, true, depot.getValue()));
            }
        }
    }

    /**
     * Method attachMarket attach the market
     * @param market of type Market is the instance of the market of the game
     */
    @Override
    public void attachMarket(Market market) {

    }

    /**
     * Method attachResourceManager attach the resource manager
     * @param resourceManager of type ResourceManager is an instance of the resource manager
     */
    @Override
    public void attachResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }




    @Override
    public String toString() {
        String x = "\ndepots= ";
        for (Resource res: depots){
            x+= "{"+ res.getType().getDisplayName()+", "+res.getValue()+"}  ";
        }
        return x;
    }
}

