package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.exception.WrongMarblesNumberException;
import it.polimi.ingsw.exception.WrongMarketDimensionException;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarbleTest {

    Market market;
    Marble blueMarble;
    Marble greyMarble;
    Marble purpleMarble;
    Marble redMarble;
    Marble whiteMarble;
    Marble yellowMarble;


    @BeforeEach
    void init() throws WrongMarketDimensionException, WrongMarblesNumberException {
        market = new Market(3,4,2,2,
                2,1,4,2);
        blueMarble = new BlueMarble(market);
        greyMarble = new GreyMarble(market);
        purpleMarble = new PurpleMarble(market);
        redMarble = new RedMarble(market);
        whiteMarble = new WhiteMarble(market);
        yellowMarble = new YellowMarble(market);
    }

    @Test
    void doMarbleAction_Blue() throws NegativeResourceException {
        blueMarble.doMarbleAction();
        assertEquals(1, market.getResourceToSend().size());
        assertEquals(1, market.getResourceToSend().get(0).getValue());
        assertEquals(ResourceType.SHIELD, market.getResourceToSend().get(0).getType());
    }

    @Test
    void doMarbleAction_Grey() throws NegativeResourceException {
        greyMarble.doMarbleAction();
        assertEquals(1, market.getResourceToSend().size());
        assertEquals(1, market.getResourceToSend().get(0).getValue());
        assertEquals(ResourceType.STONE, market.getResourceToSend().get(0).getType());
    }

    @Test
    void doMarbleAction_Purple() throws NegativeResourceException {
        purpleMarble.doMarbleAction();
        assertEquals(1, market.getResourceToSend().size());
        assertEquals(1, market.getResourceToSend().get(0).getValue());
        assertEquals(ResourceType.SERVANT, market.getResourceToSend().get(0).getType());
    }

    @Test
    void doMarbleAction_Red() throws NegativeResourceException {
        redMarble.doMarbleAction();
        assertEquals(1, market.getResourceToSend().size());
        assertEquals(1, market.getResourceToSend().get(0).getValue());
        assertEquals(ResourceType.FAITH, market.getResourceToSend().get(0).getType());
    }

    @Test
    void doMarbleAction_White() throws NegativeResourceException {
        whiteMarble.doMarbleAction();
        assertEquals(1, market.getWhiteMarbleDrew());
    }

    @Test
    void doMarbleAction_Yellow() throws NegativeResourceException {
        yellowMarble.doMarbleAction();
        assertEquals(1, market.getResourceToSend().size());
        assertEquals(1, market.getResourceToSend().get(0).getValue());
        assertEquals(ResourceType.COIN, market.getResourceToSend().get(0).getType());
    }
}