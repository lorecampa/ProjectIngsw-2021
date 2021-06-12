package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.client.data.ColorData;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * Implementation of marble interface that represent the grey marble.
 */
public class GreyMarble implements Marble{
    private final ColorData color = ColorData.GRAY;

    /**
     * Add a stone in market's resourcesToSend array.
     * @param market reference to the market of the game.
     */
    @Override
    public void doMarbleAction(Market market){
        market.addInResourcesToSend(ResourceFactory.createResource(ResourceType.STONE,1));
    }

    /**
     * Return ColorData.GRAY.
     * @return ColorData.GRAY.
     */
    @Override
    public ColorData getColorData() {
        return color;
    }
}
