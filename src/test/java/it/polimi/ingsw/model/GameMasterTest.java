package it.polimi.ingsw.model;

import it.polimi.ingsw.exception.DeckDevelopmentCardException;
import it.polimi.ingsw.exception.JsonFileModificationError;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GameMasterTest {

    GameMaster gm;
    GameMaster gmSp;

    public static class GameMasterStub extends GameMaster{
        public GameMasterStub(String ... players) throws Exception {
            super(new GameSetting(players.length),
                    Arrays.stream(players)
                            .collect(Collectors.toCollection(ArrayList::new)));
            nextPlayer();
        }
    }


    @BeforeEach
    void setUp() {
        assertDoesNotThrow(()-> gmSp = new GameMasterStub("Lorenzo"));
        assertDoesNotThrow(()-> gm = new GameMasterStub("Lorenzo","Matteo", "Davide"));

    }



    @Test
    void nextPlayerSinglePlayer() {
        gmSp.nextPlayer();
        assertEquals("Lorenzo", gmSp.getCurrentPlayer());
        gmSp.nextPlayer();
        assertEquals("Lorenzo", gmSp.getCurrentPlayer());
        gmSp.nextPlayer();
        assertEquals("Lorenzo", gmSp.getCurrentPlayer());

    }

    @Test
    void deliverLeaderCard() {
        assertEquals(16, gm.getSizeDeckLeader());
        assertEquals(3, gm.getNumActivePlayers());
        assertDoesNotThrow(()->gm.deliverLeaderCards());
        assertEquals(4, gm.getSizeDeckLeader());

    }




    @Test
    void drawToken(){
        assertEquals(0, gm.getSizeDeckToken());

        int sizeDeckToken = gmSp.getSizeDeckToken();
        assertEquals(7, sizeDeckToken);

        assertDoesNotThrow(()-> gmSp.drawToken());
        assertEquals(sizeDeckToken, gmSp.getSizeDeckToken());

    }

    @Test
    void discardDevelopment() {
        gm.discardDevelopmentSinglePlayer(Color.GREEN, 4);
        assertTrue(gm.getDeckDevelopment().get(0).get(0).isEmpty());

        assertDoesNotThrow(()->gm.discardDevelopmentSinglePlayer(Color.GREEN, 3));
        assertTrue(gm.getDeckDevelopment().get(0).get(0).isEmpty());

        assertDoesNotThrow(()->gm.discardDevelopmentSinglePlayer(Color.GREEN, 1));
        assertTrue(gm.getDeckDevelopment().get(0).get(0).isEmpty());

    }


}