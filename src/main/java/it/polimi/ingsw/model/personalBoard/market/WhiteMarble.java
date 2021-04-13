package it.polimi.ingsw.model.personalBoard.market;

public class WhiteMarble implements Marble{

    /**
     * Method that will increase the number of white marbles drew in market
     * @param market is the reference to the market of the game
     */
    @Override
    public void doMarbleAction(Market market) {
        market.increaseWhiteMarbleDrew();
    }
}
