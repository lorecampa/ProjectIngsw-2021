package it.polimi.ingsw.model.personalBoard.market;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.exception.WrongMarblesNumberException;
import it.polimi.ingsw.exception.WrongMarketDimensionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MarketTest {

    Market market;
    @BeforeEach
    void init(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        assertDoesNotThrow(() -> market = mapper.readValue(new File("src/main/resources/json/market.json"), Market.class));
    }

    @Test
    void testMarketCreation_WrongDim(){
        //wrong row
        assertThrows(WrongMarketDimensionException.class,() -> new Market(0,1,null));
        //both wrong
        assertThrows(WrongMarketDimensionException.class,() -> new Market(0,0,null));
        //row wrong
        assertThrows(WrongMarketDimensionException.class,() -> new Market(1,0,null));

    }

    @Test
    void testMarketCreation_WrongMarble(){
        ArrayList<Marble> tooManyMarbles = new ArrayList<>();
        tooManyMarbles.add(new YellowMarble());
        tooManyMarbles.add(new GreyMarble());
        tooManyMarbles.add(new WhiteMarble());
        tooManyMarbles.add(new RedMarble());
        tooManyMarbles.add(new BlueMarble());
        tooManyMarbles.add(new PurpleMarble());

        ArrayList<Marble> tooFewMarbles = new ArrayList<>();
        tooFewMarbles.add(new WhiteMarble());

        // too much marbles
        assertThrows(WrongMarblesNumberException.class, () -> new Market(2,2,tooManyMarbles));
        // too few marbles
        assertThrows(WrongMarblesNumberException.class, () -> new Market(3,4,tooFewMarbles));
    }

    @Test
    void testMarketCreation(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        assertDoesNotThrow(() -> market = mapper.readValue(new File("src/main/resources/json/market.json"), Market.class));
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
        ObjectMapper mapper = new ObjectMapper();
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
    void testGetResourceToSend(){
        assertEquals(0,market.getResourceToSend().size());
        market.insertMarbleInRow(0);
        assertNotEquals(0,market.getResourceToSend().size());
    }

    @Test
    void testMarketReset(){
        market.increaseWhiteMarbleDrew();
        market.insertMarbleInRow(0);
        market.reset();
        assertEquals(0, market.getWhiteMarbleDrew());
        assertEquals(0, market.getResourceToSend().size());
    }
}