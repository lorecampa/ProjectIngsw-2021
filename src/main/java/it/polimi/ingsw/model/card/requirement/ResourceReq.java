package it.polimi.ingsw.model.card.requirement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.CardDevData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.NotEnoughRequirementException;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Class ResourceReq defines a class that represent all requirement of type resource
 */
public class ResourceReq implements Requirement {
    @JsonIgnore
    private ResourceManager resourceManager = null;

    private final ArrayList<Resource> resourceReq;

    /**
     * Construct a Resource Requirement of specific resources.
     * @param resourceReq the resources required.
     */
    @JsonCreator
    public ResourceReq(@JsonProperty("resourceReq") ArrayList<Resource> resourceReq) {
        this.resourceReq = resourceReq;
    }

    /**
     * Control if the player has enough resources.
     * @param discount true if you want to consider the leader discount effect.
     * @throws NotEnoughRequirementException if the player doesn't have enough card or resources to satisfy
     * the requirement.
     */
    @Override
    public void checkRequirement(boolean discount) throws NotEnoughRequirementException {
        ArrayList<Resource> newResources= resourceReq.stream()
                .map(x-> ResourceFactory.createResource(x.getType(),  x.getValue()))
                .collect(Collectors.toCollection(ArrayList::new));
        resourceManager.canIAfford(newResources, discount);
    }

    /**
     * See {@link Requirement#attachResourceManager(ResourceManager)}.
     */
    @Override
    public void attachResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    /**
     * See {@link Requirement#attachCardManager(CardManager)}.
     */
    @Override
    public void attachCardManager(CardManager cardManager) {}

    /**
     * See {@link Requirement#toResourceData()}.
     */
    @Override
    public ArrayList<ResourceData> toResourceData() {
        return resourceReq.stream().map(Resource::toClient).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * See {@link Requirement#toCardDevData()}.
     */
    @Override
    public ArrayList<CardDevData> toCardDevData() {
        return null;
    }
}
