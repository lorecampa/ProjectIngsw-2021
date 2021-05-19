package it.polimi.ingsw.model.card.requirement;

import com.fasterxml.jackson.annotation.JsonCreator;
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
    private final ArrayList<Resource> resourceReq;
    private ResourceManager resourceManager = null;

    /**
     * Constructor ResourceReq creates a new ResourceReq instance
     * @param resourceReq of type ArrayList - the amount of resources required
     */
    @JsonCreator
    public ResourceReq(@JsonProperty("resourceReq") ArrayList<Resource> resourceReq) {
        this.resourceReq = resourceReq;
    }

    /**
     * Method checkRequirement control if the player has enough resources
     * @param discount of type boolean - if you want to consider the leader discount effect then it
     *                 must be true, otherwise false
     * @return boolean - true if he has them,  false if he hasn't
     */
    @Override
    public void checkRequirement(boolean discount) throws NotEnoughRequirementException {
        ArrayList<Resource> newResources= resourceReq.stream()
                .map(x-> ResourceFactory.createResource(x.getType(),  x.getValue()))
                .collect(Collectors.toCollection(ArrayList::new));
        resourceManager.canIAfford(newResources, discount);
    }

    /**
     * Method attachResourceManager attach the resource manager
     * @param resourceManager of type ResourceManager is an instance of the resource manager of the player
     */
    @Override
    public void attachResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    /**
     * Method attachCardManager attach the card manager
     * @param cardManager of type CardManager is an instance of the card manager of the player
     */
    @Override
    public void attachCardManager(CardManager cardManager) {}

    @Override
    public ArrayList<ResourceData> toResourceData() {
        return resourceReq.stream().map(Resource::toClient).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<CardDevData> toCardDevData() {
        return null;
    }

    @Override
    public String toString() {
        String x = "resourceReq= ";
        for(Resource res: resourceReq){
            x+= "{"+ res.getType().getDisplayName()+", "+res.getValue()+"}  ";
        }
        return x;
    }
}
