package it.polimi.ingsw.model.card.creationEffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.personalBoard.resourceManager.Depot;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import java.util.ArrayList;

/**
 * Class WarehouseEffect defines a class for all effect that will modify the warehouse structure
 */
public class WarehouseEffect  implements OnCreationEffect {
    private final ArrayList<Resource> depots;
    private boolean used = false;
    private ResourceManager resourceManager = null;

    /**
     * Constructor WarehouseEffect creates a new WarehouseEffect instance
     * @param depots of type ArrayList - the amount of resources that will be transformed in depots
     */
    @JsonCreator
    public WarehouseEffect(@JsonProperty("depots") ArrayList<Resource> depots) {
        this.depots = depots;
    }

    /**
     * Method doCreationEffect creates a new locked depot (lockDepot = true)  for all the resources
     * in depots and change the leader status (used = true)
     */
    @Override
    public void doCreationEffect() {
        for (Resource depot: depots){
            //clone with value = 0
            Resource res = ResourceFactory.createResource(depot.getType(), 0);
            //serve mandare eccezzioni se presenti depot con value negativi? per me no
            resourceManager.addLeaderDepot(new Depot(res, true, depot.getValue()));
        }
        this.used = true;
    }

    /**
     * Method attachResourceManager attach the resource manager
     * @param resourceManager of type ResourceManager is an instance of the resource manager
     */
    @Override
    public void attachResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }


    /**
     * Method isUsed return the status of the creation effect during the game
     * @return boolean - true if the card has been used, otherwise false
     */
    @Override
    public boolean isUsed() {
        return this.used;
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

