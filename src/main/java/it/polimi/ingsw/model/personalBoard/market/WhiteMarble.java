package it.polimi.ingsw.model.personalBoard.market;

public class WhiteMarble implements Marble{
    private final Market market;

    public WhiteMarble(Market market) {
        this.market = market;
    }

    @Override
    public void doMarbleAction() {
        market.increaseWhiteMarbleDrew();
    }
}
