package it.polimi.ingsw.model.personalBoard.faithTrack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    Cell normalCell;
    Cell victoryCell;
    Cell popeSpaceCell;
    @BeforeEach
    void init(){
        normalCell = new NormalCell(null, 0);
        victoryCell = new VictoryCell(null, 10, 1);
        popeSpaceCell = new PopeSpaceCell(null, 15, 2);
    }

    @Test
    void doAction() {
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