package it.polimi.ingsw.model.card.activationEffect;


import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;

import java.util.ArrayList;

public class ProductionEffect implements OnActivationEffect{
    private ArrayList<Resource> resourceCost;
    private ArrayList<Resource> resourceAcquired;
    private ResourceManager resourceManager = null;


    @Override
    public void doActivationEffect() throws NegativeResourceException {
        //TODO
        //going to add resources to strongbox and it will handle the user interaction
    }

    @Override
    public void attachMarket(Market market) {

    }

    @Override
    public void attachResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
}
