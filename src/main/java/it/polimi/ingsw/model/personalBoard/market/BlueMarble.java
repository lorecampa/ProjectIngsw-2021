package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.client.data.ColorData;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * Implementation of marble interface that represent the blue marble.
 */
public class BlueMarble implements Marble{
    private final ColorData color = ColorData.BLUE;

    /**
     * Add a shield in market's resourcesToSend array.
     * @param market reference to the market of the game.
     */
    @Override
    public void doMarbleAction(Market market){
        market.addInResourcesToSend(ResourceFactory.createResource(ResourceType.SHIELD,1));
    }

    /**
     * Return ColorData.BLUE.
     * @return ColorData.BLUE.
     */
    @Override
    public ColorData getColorData() {
        return color;
    }
}
