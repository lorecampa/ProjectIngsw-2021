package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.exception.WrongMarblesNumberException;
import it.polimi.ingsw.exception.WrongMarketDimensionException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MarketTest {

    Market market;
    @BeforeEach
    void init() throws WrongMarketDimensionException, WrongMarblesNumberException {
        assertDoesNotThrow(() -> new Market(3,4,2,2,2,1,4,2));
        market = new Market(3,4,2,2,2,1,4,2);
    }

    @Test
    void testMarketCreation_WrongDim(){
        //wrong row
        assertThrows(WrongMarketDimensionException.class,() -> new Market(0,1,1,2,2,1,1,1));
        //both wrong
        assertThrows(WrongMarketDimensionException.class,() -> new Market(0,0,1,2,2,1,1,1));
        //row wrong
        assertThrows(WrongMarketDimensionException.class,() -> new Market(1,0,1,2,2,1,1,1));

    }

    @Test
    void testMarketCreation_WrongMarble(){
        // too much marbles
        assertThrows(WrongMarblesNumberException.class, () -> new Market(3,4,4,2,2,2,2,2));
        // too few marbles
        assertThrows(WrongMarblesNumberException.class, () -> new Market(3,4,1,2,2,2,2,2));
    }

    @Test
    void testMarketCreation(){
        // right creation
        assertDoesNotThrow(() -> new Market(4,3,2,2,2,1,4,2));
    }


    @Test
    void testGetWhiteMarbleDrew() {
        assertEquals(0, market.getWhiteMarbleDrew());
    }

    @Test
    void testIncreaseWhiteMarbleDrew() {
        assertEquals(0, market.getWhiteMarbleDrew());
        market.increaseWhiteMarbleDrew();
        assertEquals(1, market.getWhiteMarbleDrew());
    }


    @Test
    void testInsertMarbleInRow_WrongIndex(){
        assertThrows(IndexOutOfBoundsException.class , () -> market.insertMarbleInRow(3));
        assertThrows(IndexOutOfBoundsException.class , () -> market.insertMarbleInRow(-1));
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2})
    void testInsertMarbleInRow(int index){
        assertDoesNotThrow(() -> market.insertMarbleInRow(index));

        assertNotEquals(0,market.getResourceToSend().size());
    }

    @Test
    void testInsertMarbleInCol_WrongIndex(){
        assertThrows(IndexOutOfBoundsException.class , () -> market.insertMarbleInCol(4));
        assertThrows(IndexOutOfBoundsException.class , () -> market.insertMarbleInCol(-1));
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3})
    void testInsertMarbleInCol(int index){
        assertDoesNotThrow(() -> market.insertMarbleInCol(index));

        assertNotEquals(0,market.getResourceToSend().size());
    }

    @Test
    void testGetResourceToSend() throws NegativeResourceException {
        assertEquals(0,market.getResourceToSend().size());
        market.insertMarbleInRow(0);
        assertNotEquals(0,market.getResourceToSend().size());
    }

    @Test
    void testMarketReset() throws NegativeResourceException {
        market.increaseWhiteMarbleDrew();
        market.insertMarbleInRow(0);
        market.reset();
        assertEquals(0, market.getWhiteMarbleDrew());
        assertEquals(0, market.getResourceToSend().size());
    }
}