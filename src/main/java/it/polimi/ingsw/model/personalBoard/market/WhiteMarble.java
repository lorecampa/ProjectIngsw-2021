package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.client.data.ColorData;

/**
 * Implementation of marble interface that represent the white marble.
 */
public class WhiteMarble implements Marble{
    private final ColorData color = ColorData.WHITE;

    /**
     * Increase the number of white marbles drew in market
     * @param market reference to the market of the game
     */
    @Override
    public void doMarbleAction(Market market) {
        market.increaseWhiteMarbleDrew();
    }

    /**
     * Return ColorData.WHITE.
     * @return ColorData.WHITE.
     */
    @Override
    public ColorData getColorData() {
        return color;
    }
}
