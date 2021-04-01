package it.polimi.ingsw.model.personalBoard.faithTrack;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

class FaithTrackTest {

    FaithTrack faithTrack;

    @BeforeEach
    void init(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        assertDoesNotThrow(()-> faithTrack = mapper.readValue(new File("src/main/resources/json/FaithTrack.json"), FaithTrack.class));
    }

    @Test
    void testCreation(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        assertDoesNotThrow(()-> faithTrack = mapper.readValue(new File("src/main/resources/json/FaithTrack.json"), FaithTrack.class));
    }

    @ParameterizedTest
    @ValueSource(ints = {0,3,6,8,9,12,15,16,18,21,24})
    void testMovePlayer(int positions){
        faithTrack.movePlayer(positions);
        switch (positions) {
            case 0: assertEquals(0,faithTrack.getVictoryPoints());break;
            case 3: assertEquals(1,faithTrack.getVictoryPoints());break;
            case 6:
            case 8:
                assertEquals(2,faithTrack.getVictoryPoints());break;
            case 9: assertEquals(4,faithTrack.getVictoryPoints());break;
            case 12: assertEquals(6,faithTrack.getVictoryPoints());break;
            case 15:
            case 16:
                assertEquals(9,faithTrack.getVictoryPoints());break;
            case 18: assertEquals(12,faithTrack.getVictoryPoints());break;
            case 21: assertEquals(16,faithTrack.getVictoryPoints());break;
            case 24: assertEquals(20,faithTrack.getVictoryPoints());break;
        }
    }

    @Test
    void testIncreasePlayerPosition(){
        assertEquals(0,faithTrack.getCurrentPositionOnTrack());
        faithTrack.increasePlayerPosition();
        assertEquals(1,faithTrack.getCurrentPositionOnTrack());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2})
    void testAddVPForPopeFavor(int popeFavorNum){
        faithTrack.addVPForPopeFavor(popeFavorNum);
        switch (popeFavorNum){
            case 0: assertEquals(2, faithTrack.getPopeFavorVP());break;
            case 1: assertEquals(3,faithTrack.getPopeFavorVP());break;
            case 2: assertEquals(4,faithTrack.getPopeFavorVP());break;
        }
    }

}