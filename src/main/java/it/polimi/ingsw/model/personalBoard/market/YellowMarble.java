package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.client.data.ColorData;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * Implementation of marble interface that represent the yellow marble.
 */
public class YellowMarble implements Marble{
    private final ColorData color = ColorData.YELLOW;

    /**
     * Add a coin in market's resourcesToSend array.
     * @param market reference to the market of the game
     */
    @Override
    public void doMarbleAction(Market market){
        market.addInResourcesToSend(ResourceFactory.createResource(ResourceType.COIN,1));
    }

    /**
     * Return ColorData.YELLOW.
     * @return ColorData.YELLOW.
     */
    @Override
    public ColorData getColorData() {
        return color;
    }
}
