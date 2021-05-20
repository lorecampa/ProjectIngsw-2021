package it.polimi.ingsw.model;

import it.polimi.ingsw.exception.DeckDevelopmentCardException;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameMasterTest {

    GameMaster gm;
    GameMaster gmSp;
    GameSetting gs;
    GameSetting gsSinglePlayer;
    ArrayList<String> players = new ArrayList<>();
    int numOfPlayer;
    @BeforeEach
    void setUp() throws IOException {
        assertDoesNotThrow(()-> gsSinglePlayer = new GameSetting(1));
        players.add("Lorenzo");
        gmSp = new GameMaster(gsSinglePlayer, players);

        numOfPlayer = 3;
        players.add("Matteo");
        players.add("Davide");
        assertDoesNotThrow(()-> gs = new GameSetting(numOfPlayer));
        gm = new GameMaster(gs, players);
        gm.nextPlayer();


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

        assertDoesNotThrow(()->gm.discardDevelopmentSinglePlayer(Color.GREEN, 5));

        assertDoesNotThrow(()->gm.discardDevelopmentSinglePlayer(Color.BLUE, 9));
        assertDoesNotThrow(()->gm.discardDevelopmentSinglePlayer(Color.PURPLE, 12));

    }



    @Test
    void updateFromResourceManager() {
        gm.discardResources(6);
        for(String player: players){
            if (!player.equals(gm.getCurrentPlayer())){
                assertEquals(6, gm.getPlayerPersonalBoard(player).getFaithTrack().getCurrentPositionOnTrack());
            }
        }
    }

}