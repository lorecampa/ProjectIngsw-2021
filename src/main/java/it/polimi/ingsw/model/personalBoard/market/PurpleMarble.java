package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

public class PurpleMarble implements Marble{
    private final Market market;

    public PurpleMarble(Market market) {
        this.market = market;
    }

    @Override
    public void doMarbleAction() throws NegativeResourceException {
        market.addInResourcesToSend(ResourceFactory.createResource(ResourceType.SERVANT,1));
    }
}
