package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void getColumnDeckDevelopment() {
        assertEquals(0, Color.GREEN.getColumnDeckDevelopment());
        assertEquals(1, Color.PURPLE.getColumnDeckDevelopment());
        assertEquals(2, Color.BLUE.getColumnDeckDevelopment());
        assertEquals(3, Color.YELLOW.getColumnDeckDevelopment());
        assertEquals(-1, Color.ANY.getColumnDeckDevelopment());
    }
}