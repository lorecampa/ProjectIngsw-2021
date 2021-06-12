package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.client.data.ColorData;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * Implementation of marble interface that represent the purple marble.
 */
public class PurpleMarble implements Marble{
    private final ColorData color = ColorData.PURPLE;

    /**
     * Add a servant in market's resourcesToSend array.
     * @param market reference to the market of the game.
     */
    @Override
    public void doMarbleAction(Market market){
        market.addInResourcesToSend(ResourceFactory.createResource(ResourceType.SERVANT,1));
    }

    /**
     * Return ColorData.PURPLE.
     * @return ColorData.PURPLE.
     */
    @Override
    public ColorData getColorData() {
        return color;
    }
}
