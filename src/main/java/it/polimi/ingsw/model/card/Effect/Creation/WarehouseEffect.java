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
import it.polimi.ingsw.model.personalBoard.resourceManager.Depot;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * WarehouseEffect class represent all effects that modify the warehouse structure.
 */
public class WarehouseEffect  implements Effect {
    @JsonIgnore
    private ResourceManager resourceManager = null;

    private final ArrayList<Resource> depots;

    /**
     * Construct a warehouse effect with specific resources.
     * @param depots the amount of resources that will be transformed in depot.
     */
    @JsonCreator
    public WarehouseEffect(@JsonProperty("depots") ArrayList<Resource> depots) {
        this.depots = depots;
    }

    /**
     * Return the effect depot.
     * @return the effect depot.
     */
    public ArrayList<Resource> getDepots() {
        return depots;
    }

    /**
     * Creates a new locked depot (lockDepot = true) for all the resources in depots.
     * @param playerState the state of the turn, in this case must be of type CREATION_STATE.
     */
    @Override
    public void doEffect(PlayerState playerState) {
        if (playerState == PlayerState.LEADER_MANAGE_BEFORE){
            resourceManager.addLeaderDepot(depots.stream()
                    .map(x->new Depot(ResourceFactory.createResource(x.getType(), 0), x.getValue()))
                    .collect(Collectors.toCollection(ArrayList::new)));
        }
    }

    /**
     * See {@link Effect#attachMarket(Market)}.
     */
    @Override
    public void attachMarket(Market market) {
    }

    /**
     * See {@link Effect#attachResourceManager(ResourceManager)}.
     */
    @Override
    public void attachResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    /**
     * See {@link Effect#toEffectData()}.
     */
    @Override
    public EffectData toEffectData() {
        String description = "Warehouse effect: ";
        ArrayList<ResourceData> depot = depots.stream().map(Resource::toClient).collect(Collectors.toCollection(ArrayList::new));
        return new EffectData(EffectType.WAREHOUSE,description,depot,null);
    }

}

