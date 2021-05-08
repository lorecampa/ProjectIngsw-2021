package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.client.data.ColorData;

public class WhiteMarble implements Marble{
    private final ColorData color = ColorData.WHITE;
    /**
     * Method that will increase the number of white marbles drew in market
     * @param market is the reference to the market of the game
     */
    @Override
    public void doMarbleAction(Market market) {
        market.increaseWhiteMarbleDrew();
    }

    @Override
    public ColorData getColorData() {
        return color;
    }
}
