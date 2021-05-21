package it.polimi.ingsw.model.card.Effect.Creation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.EffectData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.TurnState;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.Depot;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Class WarehouseEffect defines a class for all effect that modify the warehouse structure
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
     * @param turnState of type State - defines the state of the turn, in this case must be of type CREATION_STATE
     */
    @Override
    public void doEffect(TurnState turnState) {
        if (turnState == TurnState.LEADER_MANAGE_BEFORE){
            resourceManager.addLeaderDepot(depots.stream()
                    .map(x->new Depot(ResourceFactory.createResource(x.getType(), 0), x.getValue()))
                    .collect(Collectors.toCollection(ArrayList::new)));
        }
    }

    @Override
    public void discardEffect() {
        resourceManager.removeLeaderDepot(depots.stream()
                .map(x->new Depot(ResourceFactory.createResource(x.getType(), 0), x.getValue()))
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    /**
     * Method attachMarket does nothing because the production effect doesn't need it
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

    public ArrayList<Resource> getDepots() {
        return depots;
    }

    @Override
    public EffectData toEffectData() {
        String description = "Warehouse effect: ";
        ArrayList<ResourceData> depot = depots.stream().map(Resource::toClient).collect(Collectors.toCollection(ArrayList::new));
        return new EffectData(description,depot,null);
    }

}

