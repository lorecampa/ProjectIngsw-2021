package it.polimi.ingsw.model.card.requirement;


import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import java.util.ArrayList;


public class ResourceReq implements Requirement {
    private ArrayList<Resource> resourceReq;
    private ResourceManager resourceManager = null;


    @Override
    public boolean checkRequirement() {
        return  true;
        //TODO
        //control personalBoard warehouse and strongbox resources if we have enough resources
    }

    @Override
    public void attachResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @Override
    public void attachCardManager(CardManager cardManager) {

    }
}
