package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

public class RedMarble implements Marble{

    @Override
    public void doMarbleAction(Market market) throws NegativeResourceException {
        market.addInResourcesToSend(ResourceFactory.createResource(ResourceType.FAITH,1));
    }
}
