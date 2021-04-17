package it.polimi.ingsw.model;

import it.polimi.ingsw.exception.DeckDevelopmentCardException;
import it.polimi.ingsw.exception.JsonFileConfigError;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.token.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameMasterTest {

    GameMaster gm;
    int numOfPlayer;
    @BeforeEach
    void setUp() throws IOException, JsonFileConfigError {
        numOfPlayer = 4;
        gm = new GameMaster("player1", numOfPlayer);
        gm.setCurrentPlayer("player1");

        gm.addPlayer("player2");
        gm.addPlayer("player3");
        gm.addPlayer("player4");


        gm.getPlayerPersonalBoard("player1").getFaithTrack().attachObserver(gm);
        gm.getPlayerPersonalBoard("player2").getFaithTrack().attachObserver(gm);
        gm.getPlayerPersonalBoard("player3").getFaithTrack().attachObserver(gm);
        gm.getPlayerPersonalBoard("player4").getFaithTrack().attachObserver(gm);


    }

    @Test
    void nextPlayer() {
        gm.nextPlayer();
        assertEquals("player2", gm.getCurrentPlayer());
        gm.nextPlayer();
        assertEquals("player3", gm.getCurrentPlayer());
        gm.nextPlayer();
        assertEquals("player4", gm.getCurrentPlayer());
        gm.nextPlayer();
        assertEquals("player1", gm.getCurrentPlayer());
    }

    @Test
    void deliverLeaderCard() {
        assertEquals(16, gm.getSizeDeckLeader());
        assertEquals(4, gm.getNumActivePlayers());
        gm.deliverLeaderCards();
        assertEquals(0, gm.getSizeDeckLeader());

    }

    @Test
    void popDeckDevelopmentCard()  {
        //deck development is Matrix(3,4)
        assertThrows(IndexOutOfBoundsException.class, ()-> gm.popDeckDevelopmentCard(4, 4));
        assertThrows(IndexOutOfBoundsException.class, ()-> gm.popDeckDevelopmentCard(-1, 4));
        assertThrows(IndexOutOfBoundsException.class, ()-> gm.popDeckDevelopmentCard(2, -1));
        assertDoesNotThrow(()-> gm.popDeckDevelopmentCard(2, 3));

        assertDoesNotThrow(()->gm.removeDeckDevelopmentCard(1,1));
        assertDoesNotThrow(()->gm.removeDeckDevelopmentCard(1,1));
        assertDoesNotThrow(()->gm.removeDeckDevelopmentCard(1,1));
        assertDoesNotThrow(()->gm.removeDeckDevelopmentCard(1,1));

        //try to pop where there's no card
        assertThrows(DeckDevelopmentCardException.class, ()-> gm.popDeckDevelopmentCard(1, 1));


    }

    @Test
    void removeDeckDevelopmentCard() {
        Development dev = gm.getDeckDevelopment().get(1).get(1).get(1);
        assertDoesNotThrow(()->gm.removeDeckDevelopmentCard(1,1));
        assertSame(dev, gm.getDeckDevelopment().get(1).get(1).get(0));

        assertDoesNotThrow(()->gm.removeDeckDevelopmentCard(2,2));
        assertDoesNotThrow(()->gm.removeDeckDevelopmentCard(2,2));
        assertDoesNotThrow(()->gm.removeDeckDevelopmentCard(2,2));

        assertDoesNotThrow(()-> gm.popDeckDevelopmentCard(2, 2));
        assertThrows(DeckDevelopmentCardException.class, ()-> gm.removeDeckDevelopmentCard(2, 2));

        assertThrows(IndexOutOfBoundsException.class, ()-> gm.removeDeckDevelopmentCard(2, -1));
        assertThrows(IndexOutOfBoundsException.class, ()-> gm.removeDeckDevelopmentCard(3, 2));

    }

    @Test
    void pushDeckDevelopmentCard() throws DeckDevelopmentCardException {
        //if size is already 4
        Development dev = gm.popDeckDevelopmentCard(1,1);
        assertThrows(DeckDevelopmentCardException.class, ()-> gm.pushDeckDevelopmentCard(2, 2, dev));

        assertDoesNotThrow(()->gm.removeDeckDevelopmentCard(2,3));
        assertDoesNotThrow(()->gm.pushDeckDevelopmentCard(2, 3, dev));
        assertSame(gm.popDeckDevelopmentCard(2,3), dev);

        assertThrows(IndexOutOfBoundsException.class, ()-> gm.pushDeckDevelopmentCard(4, 2, dev));

    }

    @Test
    void drawToken() throws IOException, JsonFileConfigError {
        assertEquals(0, gm.getSizeDeckToken());
        GameMaster gmSp = new GameMaster("Single Player", 1);
        int sizeDeckToken = gmSp.getSizeDeckToken();
        gmSp.drawToken();
        assertEquals(sizeDeckToken, gmSp.getSizeDeckToken());

    }

    @Test
    void discardDevelopment() throws DeckDevelopmentCardException {
        gm.discardDevelopment(Color.GREEN, 4);
        assertTrue(gm.getDeckDevelopment().get(0).get(0).isEmpty());

        assertDoesNotThrow(()->gm.discardDevelopment(Color.GREEN, 5));
        assertThrows(DeckDevelopmentCardException.class, ()->gm.discardDevelopment(Color.GREEN, 4));

        assertDoesNotThrow(()->gm.discardDevelopment(Color.BLUE, 9));
        assertDoesNotThrow(()->gm.discardDevelopment(Color.PURPLE, 12));
        assertThrows(DeckDevelopmentCardException.class, ()->gm.discardDevelopment(Color.YELLOW, 13));

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
        gm.updateFromResourceManager(6);
        assertEquals(0, gm.getPlayerPersonalBoard("player1").getFaithTrack().getVictoryPoints());
        assertEquals(2, gm.getPlayerPersonalBoard("player2").getFaithTrack().getVictoryPoints());
        assertEquals(2, gm.getPlayerPersonalBoard("player3").getFaithTrack().getVictoryPoints());
        assertEquals(2, gm.getPlayerPersonalBoard("player4").getFaithTrack().getVictoryPoints());
    }

    @Test
    void updateFromCardManager() {
        gm.updateFromCardManager();
        assertEquals(1, gm.getPlayerPersonalBoard("player1").getFaithTrack().getCurrentPositionOnTrack());
        gm.getPlayerPersonalBoard("player1").getFaithTrack().movePlayer(4);
        gm.updateFromCardManager();
        assertEquals(2, gm.getPlayerPersonalBoard("player1").getFaithTrack().getVictoryPoints());
    }
}