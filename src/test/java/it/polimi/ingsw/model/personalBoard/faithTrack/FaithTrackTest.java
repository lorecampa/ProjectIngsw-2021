package it.polimi.ingsw.model.personalBoard.faithTrack;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.client.data.FaithTrackData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

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

    @ParameterizedTest
    @ValueSource(ints = {0,3,6,8,9,12,15,16,18,21,24})
    void testDoCurrentCellAction(int positions){
        for (int i = 0; i < positions; i++) {
            faithTrack.increasePlayerPosition();
        }
        faithTrack.doCurrentCellAction();
        switch (positions){
            case 0:
            case 8:
            case 16:
                assertEquals(0,faithTrack.getVictoryPoints());break;
            case 3: assertEquals(1,faithTrack.getVictoryPoints());break;
            case 6: assertEquals(2,faithTrack.getVictoryPoints());break;
            case 9: assertEquals(4,faithTrack.getVictoryPoints());break;
            case 12: assertEquals(6,faithTrack.getVictoryPoints());break;
            case 15: assertEquals(9,faithTrack.getVictoryPoints());break;
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
    @ValueSource(ints = {0,5,10,14,17,20})
    void testPopeFavorActivated(int positions) {
        faithTrack.movePlayer(positions);
        faithTrack.popeFavorActivated(0);
        faithTrack.popeFavorActivated(1);
        faithTrack.popeFavorActivated(2);

        switch (positions){
            case 0:
            case 10:
            case 17:
                assertEquals(0,faithTrack.getPopeFavorVP()); break;
            case 5: assertEquals(2,faithTrack.getPopeFavorVP()); break;
            case 14: assertEquals(3,faithTrack.getPopeFavorVP()); break;
            case 20: assertEquals(4,faithTrack.getPopeFavorVP()); break;
        }
    }

    @Test
    public void toDataFaith(){
        ArrayList<FaithTrackData> faithTrackData = faithTrack.toFaithTrackData();
    }
}