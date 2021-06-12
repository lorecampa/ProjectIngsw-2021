package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.client.data.ColorData;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * Implementation of marble interface that represent the red marble.
 */
public class RedMarble implements Marble{
    private final ColorData color = ColorData.RED;

    /**
     * Add a faith in market's resourcesToSend array.
     * @param market reference to the market of the game.
     */
    @Override
    public void doMarbleAction(Market market){
        market.addInResourcesToSend(ResourceFactory.createResource(ResourceType.FAITH,1));
    }

    /**
     * Return ColorData.RED.
     * @return ColorData.RED.
     */
    @Override
    public ColorData getColorData() {
        return color;
    }
}
