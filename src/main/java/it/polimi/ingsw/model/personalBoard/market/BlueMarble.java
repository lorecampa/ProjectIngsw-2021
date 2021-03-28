package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

public class BlueMarble implements Marble{
    private final Market market;

    public BlueMarble(Market market) {
        this.market = market;
    }

    @Override
    public void doMarbleAction() throws NegativeResourceException {
        market.addInResourcesToSend(ResourceFactory.createResource(ResourceType.SHIELD,1));
    }
}
