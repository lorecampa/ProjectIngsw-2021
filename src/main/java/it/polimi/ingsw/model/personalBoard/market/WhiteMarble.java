package it.polimi.ingsw.model.personalBoard.market;

public class WhiteMarble implements Marble{

    @Override
    public void doMarbleAction(Market market) {
        market.increaseWhiteMarbleDrew();
    }
}
