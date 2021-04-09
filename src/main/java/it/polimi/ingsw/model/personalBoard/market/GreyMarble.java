package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

public class GreyMarble implements Marble{

    @Override
    public void doMarbleAction(Market market){
        market.addInResourcesToSend(ResourceFactory.createResource(ResourceType.STONE,1));
    }
}
