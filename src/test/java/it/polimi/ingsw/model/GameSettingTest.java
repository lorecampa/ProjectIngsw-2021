package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameSettingTest {

    GameSetting gs;
    @BeforeEach
    @Test
    void init(){
        assertDoesNotThrow(()->gs = new GameSetting(4));
    }

    @Test
    void getBaseProduction() {
        assertEquals(3, gs.getDeckDevelopment().size());
        assertEquals(4, gs.getDeckDevelopment().get(0).size());
        assertEquals(4, gs.getDeckDevelopment().get(0).get(0).size());

        assertEquals(16, gs.getDeckLeader().size());

        assertEquals(0, gs.getDeckToken().size());

        assertDoesNotThrow(()-> gs = new GameSetting(1));
        assertEquals(6, gs.getDeckToken().size());

    }



}