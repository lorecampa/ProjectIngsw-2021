package it.polimi.ingsw.model.card.requirement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import java.util.ArrayList;

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
     * @return boolean - true if he has them,  false if he hasn't
     * @throws NegativeResourceException when the resources in resourceReq contain negative values
     */
    @Override
    public boolean checkRequirement() throws NegativeResourceException {
        //TODO
        //matteo deve cambiarlo e mettere un parametro boolean per dire che sono un Leader
        return resourceManager.canIAfford(resourceReq, true);
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
     * @param cardManager of type CardManager is an istance of the card manager of the player
     */
    @Override
    public void attachCardManager(CardManager cardManager) {}


    @Override
    public String toString() {
        String x = "resourceReq= ";
        for(Resource res: resourceReq){
            x+= "{"+ res.getType().getDisplayName()+", "+res.getValue()+"}  ";
        }
        return x;
    }
}
