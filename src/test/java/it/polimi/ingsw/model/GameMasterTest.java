package it.polimi.ingsw.model;

import it.polimi.ingsw.exception.DeckDevelopmentCardException;
import it.polimi.ingsw.exception.JsonFileModificationError;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameMasterTest {

    GameMaster gm;
    GameMaster gmSinglePlayer;
    GameSetting gs;
    GameSetting gsSinglePlayer;
    ArrayList<String> players = new ArrayList<>();
    int numOfPlayer;
    @BeforeEach
    void setUp() throws IOException, JsonFileModificationError {
        assertDoesNotThrow(()-> gsSinglePlayer = new GameSetting(1));
        players.add("Lorenzo");
        gmSinglePlayer = new GameMaster(gsSinglePlayer, players);

        numOfPlayer = 3;
        players.add("Matteo");
        players.add("Davide");
        assertDoesNotThrow(()-> gs = new GameSetting(numOfPlayer));
        gm = new GameMaster(gs, players);
        gm.nextPlayer();


    }



    @Test
    void nextPlayerSinglePlayer() {
        gmSinglePlayer.nextPlayer();
        assertEquals("Lorenzo", gm.getCurrentPlayer());
        gmSinglePlayer.nextPlayer();
        assertEquals("Lorenzo", gm.getCurrentPlayer());
        gmSinglePlayer.nextPlayer();
        assertEquals("Lorenzo", gm.getCurrentPlayer());

    }

    @Test
    void deliverLeaderCard() {
        assertEquals(16, gm.getSizeDeckLeader());
        assertEquals(4, gm.getNumActivePlayers());
        assertDoesNotThrow(()->gm.deliverLeaderCards());
        assertEquals(0, gm.getSizeDeckLeader());

    }


    @Test
    void pushDeckDevelopmentCard() throws DeckDevelopmentCardException {
        Development dev = gm.getDeckDevelopmentCard(1,1);
        //if size is already 4
        assertThrows(DeckDevelopmentCardException.class, ()-> gm.pushDeckDevelopmentCard(2, 2, dev));

        assertDoesNotThrow(()-> gm.onDeckDevelopmentCardRemove(2, 3));

        assertDoesNotThrow(()->gm.pushDeckDevelopmentCard(2, 3, dev));

        assertThrows(IndexOutOfBoundsException.class, ()-> gm.pushDeckDevelopmentCard(4, 2, dev));

    }

    @Test
    void drawToken() throws IOException, JsonFileModificationError {
        assertEquals(0, gm.getSizeDeckToken());

        int sizeDeckToken = gmSinglePlayer.getSizeDeckToken();
        assertEquals(6, sizeDeckToken);

        gmSinglePlayer.drawToken();
        assertEquals(sizeDeckToken, gmSinglePlayer.getSizeDeckToken());

    }

    @Test
    void discardDevelopment() throws DeckDevelopmentCardException {
        gm.discardDevelopmentSinglePlayer(Color.GREEN, 4);
        assertTrue(gm.getDeckDevelopment().get(0).get(0).isEmpty());

        assertDoesNotThrow(()->gm.discardDevelopmentSinglePlayer(Color.GREEN, 5));
        assertThrows(DeckDevelopmentCardException.class, ()->gm.discardDevelopmentSinglePlayer(Color.GREEN, 4));

        assertDoesNotThrow(()->gm.discardDevelopmentSinglePlayer(Color.BLUE, 9));
        assertDoesNotThrow(()->gm.discardDevelopmentSinglePlayer(Color.PURPLE, 12));
        assertThrows(DeckDevelopmentCardException.class, ()->gm.discardDevelopmentSinglePlayer(Color.YELLOW, 13));

    }

    @Test
    void updateFromFaithTrack() {
        gm.getPlayerPersonalBoard("player4").getFaithTrack().movePlayer(5);
        gm.getPlayerPersonalBoard("player3").getFaithTrack().movePlayer(6);
        gm.getPlayerPersonalBoard("player2").getFaithTrack().movePlayer(7);
        gm.getPlayerPersonalBoard("player1").getFaithTrack().movePlayer(8);

        assertEquals(2, gm.getPlayerPersonalBoard("player1").getFaithTrack().getPopeFavorVP());
        assertEquals(2, gm.getPlayerPersonalBoard("player2").getFaithTrack().getPopeFavorVP());
        assertEquals(2, gm.getPlayerPersonalBoard("player3").getFaithTrack().getPopeFavorVP());
        assertEquals(2, gm.getPlayerPersonalBoard("player4").getFaithTrack().getPopeFavorVP());
    }

    @Test
    void updateFromResourceManager() {
        gm.discardResources(6);
        assertEquals(0, gm.getPlayerPersonalBoard("Lorenzo").getFaithTrack().getVictoryPoints());
        assertEquals(2, gm.getPlayerPersonalBoard("Davide").getFaithTrack().getVictoryPoints());
        assertEquals(2, gm.getPlayerPersonalBoard("Matteo").getFaithTrack().getVictoryPoints());

    }

}