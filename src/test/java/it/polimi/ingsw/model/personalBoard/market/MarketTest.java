package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.exception.WrongMarblesNumberException;
import it.polimi.ingsw.exception.WrongMarketDimensionException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MarketTest {

    Market market;
    @BeforeEach
    void init() throws WrongMarketDimensionException, WrongMarblesNumberException {
        market = new Market(4,3,2,2,2,1,4,2);
    }

    @Test
    void testMarketCreation(){
        assertThrows(WrongMarketDimensionException.class,() -> new Market(0,1,1,2,2,1,1,1));
        assertThrows(WrongMarketDimensionException.class,() -> new Market(0,0,1,2,2,1,1,1));
        assertThrows(WrongMarketDimensionException.class,() -> new Market(1,0,1,2,2,1,1,1));

        assertThrows(WrongMarblesNumberException.class, () -> new Market(3,4,4,2,2,2,2,2));
        assertThrows(WrongMarblesNumberException.class, () -> new Market(3,4,1,2,2,2,2,2));
    }

    @Test
    void testAddInResourcesToSend(){
        Resource res = ResourceFactory.createResource(ResourceType.COIN,3);
    }

    @Test
    void getWhiteMarbleDrew() {
        assertEquals(0, market.getWhiteMarbleDrew());
    }

    @Test
    void increaseWhiteMarbleDrew() {
        assertEquals(0, market.getWhiteMarbleDrew());
        market.increaseWhiteMarbleDrew();
        assertEquals(1, market.getWhiteMarbleDrew());
    }


}