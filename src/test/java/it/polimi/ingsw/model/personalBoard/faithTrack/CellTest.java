package it.polimi.ingsw.model.personalBoard.faithTrack;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    Cell normalCell;
    Cell victoryCell;
    Cell popeSpaceCell;
    Cell popeSpaceCell_2;
    FaithTrack faithTrack;
    @BeforeEach
    void init(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        assertDoesNotThrow(()-> faithTrack = mapper.readValue(new File("src/main/resources/json/FaithTrack.json"), FaithTrack.class));

        normalCell = new NormalCell(0);
        victoryCell = new VictoryCell(1,1);
        popeSpaceCell = new PopeSpaceCell(1,2);
        popeSpaceCell_2 = new PopeSpaceCell(-1,2);
    }

    @Test
    void doAction_victoryCell() {
        victoryCell.doAction(faithTrack);
        assertEquals(1, faithTrack.getVictoryPoints());
    }

    @Test
    void doAction_popeCell(){
        popeSpaceCell.doAction(faithTrack);
        assertEquals(1,faithTrack.getVictoryPoints());
        popeSpaceCell_2.doAction(faithTrack);
        assertEquals(1,faithTrack.getVictoryPoints());
    }

    @Test
    void isInVaticanReport() {
        assertTrue(normalCell.isInVaticanReport(0));
        assertFalse(normalCell.isInVaticanReport(1));

        assertTrue(victoryCell.isInVaticanReport(1));
        assertFalse(victoryCell.isInVaticanReport(2));

        assertTrue(popeSpaceCell.isInVaticanReport(2));
        assertFalse(popeSpaceCell.isInVaticanReport(1));
    }
}