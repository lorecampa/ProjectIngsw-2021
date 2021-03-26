package it.polimi.ingsw.model.card.requirement;

import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

public class ResourceReq implements Requirement {
    private int resourceReq;
    private ResourceManager resourceManager;

    public ResourceReq(int resourceReq) {
        this.resourceReq = resourceReq;
        this.resourceManager = null;
    }


    @Override
    public boolean checkRequirement() {
        return  true;
        //TODO
        //control personalBoard warehouse and strongbox resources
    }

    @Override
    public void attachResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @Override
    public void cardManager(CardManager cardManager) {

    }
}
