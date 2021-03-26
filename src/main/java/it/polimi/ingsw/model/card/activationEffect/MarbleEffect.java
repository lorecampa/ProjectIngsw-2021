package it.polimi.ingsw.model.card.activationEffect;

import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;

import java.util.ArrayList;

public class MarbleEffect implements OnActivationEffect{
    private Market market;
    private ArrayList<Resource> transformIn;

    public MarbleEffect(ArrayList<Resource> transformIn) {
        this.market = null;
        this.transformIn = transformIn;
    }

    @Override
    public void doActivationEffect() {
        //TODO
        // going ot add resources to resourcesToSend in market
    }

    @Override
    public void attachMarket(Market market) {
        this.market = market;
    }

    @Override
    public void attachResourceManager(ResourceManager resourceManager) {

    }
}
