package it.polimi.ingsw.model.card.activationEffect;

import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import java.util.ArrayList;

public class MarbleEffect implements OnActivationEffect{
    private ArrayList<Resource> transformIn;
    private Market market = null;


    @Override
    public void doActivationEffect() throws NegativeResourceException {
        //value of the new resource
        int whiteMarble = market.getWhiteMarbleDrew();
        //add resource in market
        for (Resource res: transformIn){
            market.addInResourcesToSend(ResourceFactory.createResource(res.getType(), res.getValue()*whiteMarble));

        }
    }

    @Override
    public void attachMarket(Market market) {
        this.market = market;
    }

    @Override
    public void attachResourceManager(ResourceManager resourceManager) {}
}
