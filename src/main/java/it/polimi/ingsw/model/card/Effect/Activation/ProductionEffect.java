package it.polimi.ingsw.model.card.Effect.Activation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.EffectData;
import it.polimi.ingsw.client.data.EffectType;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.NotEnoughRequirementException;
import it.polimi.ingsw.model.PlayerState;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * ProductionEffect class represent the effect of production.
 */
public class ProductionEffect implements Effect {
    @JsonIgnore
    private ResourceManager resourceManager = null;

    private final ArrayList<Resource>  resourceCost;
    private final ArrayList<Resource> resourceAcquired;

    /**
     * Construct a production effect with a specific production.
     * @param resourceCost the resources required for the production.
     * @param resourceAcquired the resources that the player will gain.
     */
    @JsonCreator
    public ProductionEffect(@JsonProperty("resourceCost") ArrayList<Resource> resourceCost,
                            @JsonProperty("resourceAcquired") ArrayList<Resource> resourceAcquired) {
        this.resourceCost = resourceCost;
        this.resourceAcquired = resourceAcquired;
    }


    /**
     * Checks if the player has enough resource for the production, if it does
     * then pass all the resource that he will gain to the resource manager and it will handle those
     * putting them into the strongbox, otherwise throws CantMakeProductionException.
     * @param playerState the state of the turn, in this case must be of type PRODUCTION_STATE.
     * @throws NotEnoughRequirementException if the player doesn't have enough card or resources to satisfy
     * all requirements of the effect.
     */
    @Override
    public void doEffect(PlayerState playerState) throws NotEnoughRequirementException {
        if (playerState == PlayerState.PRODUCTION_ACTION || playerState == PlayerState.LEADER_MANAGE_BEFORE){
            ArrayList<Resource> resourceCostCopy = resourceCost.stream()
                    .map(res -> ResourceFactory.createResource(res.getType(), res.getValue()))
                    .collect(Collectors.toCollection(ArrayList::new));

            resourceManager.canIAfford(resourceCostCopy, false);

            resourceManager.addToResourcesToProduce(resourceAcquired.stream()
                    .map(x -> ResourceFactory.createResource(x.getType(), x.getValue()))
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
     * See {@link Effect#toEffectData()}.
     */
    @Override
    public EffectData toEffectData() {
        String description = "Production effect: ";
        ArrayList<ResourceData> productionCost = resourceCost.stream().map(Resource::toClient).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<ResourceData> productionEarn = resourceAcquired.stream().map(Resource::toClient).collect(Collectors.toCollection(ArrayList::new));
        return new EffectData(EffectType.PRODUCTION,description,productionCost, productionEarn);
    }
}
