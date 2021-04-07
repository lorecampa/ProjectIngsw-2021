package it.polimi.ingsw.model;

import it.polimi.ingsw.exception.DeckDevelopmentCardException;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameMasterTest {

    GameMaster gm;
    int numOfPlayer;
    @BeforeEach
    void setUp() throws IOException {
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
        gm.setCurrentPlayer("player4");
        gm.nextPlayer();
        assertEquals("player1", gm.getCurrentPlayer());
    }

    @Test
    void deliverLeaderCard() {
        assertEquals(16, gm.getDeckLeader().size());
        assertEquals(4, gm.getNumActivePlayers());
        gm.deliverLeaderCards();
        assertEquals(0, gm.getDeckLeader().size());

    }

    @Test
    void popDeckDevelopmentCard() throws DeckDevelopmentCardException {

        assertThrows(IndexOutOfBoundsException.class, ()-> gm.popDeckDevelopmentCard(4, 4));
        assertThrows(IndexOutOfBoundsException.class, ()-> gm.popDeckDevelopmentCard(-1, 4));
        assertThrows(IndexOutOfBoundsException.class, ()-> gm.popDeckDevelopmentCard(2, -1));
        assertDoesNotThrow(()-> gm.popDeckDevelopmentCard(2, 3));

        gm.removeDeckDevelopmentCard(1,1);
        gm.removeDeckDevelopmentCard(1,1);
        gm.removeDeckDevelopmentCard(1,1);
        gm.removeDeckDevelopmentCard(1,1);
        assertThrows(DeckDevelopmentCardException.class, ()-> gm.popDeckDevelopmentCard(1, 1));


    }

    @Test
    void removeDeckDevelopmentCard() throws DeckDevelopmentCardException {
        Development dev = gm.getDeckDevelopment().get(1).get(1).get(1);
        gm.removeDeckDevelopmentCard(1,1);
        assertSame(dev, gm.getDeckDevelopment().get(1).get(1).get(0));
        gm.removeDeckDevelopmentCard(2,2);
        gm.removeDeckDevelopmentCard(2,2);
        gm.removeDeckDevelopmentCard(2, 2);
        assertDoesNotThrow(()-> gm.popDeckDevelopmentCard(2, 2));
        assertThrows(DeckDevelopmentCardException.class, ()-> gm.removeDeckDevelopmentCard(2, 2));

        assertThrows(IndexOutOfBoundsException.class, ()-> gm.popDeckDevelopmentCard(2, -1));
        assertThrows(IndexOutOfBoundsException.class, ()-> gm.popDeckDevelopmentCard(3, 2));

    }

    @Test
    void pushDeckDevelopmentCard() throws DeckDevelopmentCardException {
        Development dev = gm.popDeckDevelopmentCard(1,1);
        assertThrows(DeckDevelopmentCardException.class, ()-> gm.pushDeckDevelopmentCard(2, 2, dev));
        gm.removeDeckDevelopmentCard(2,3);
        gm.pushDeckDevelopmentCard(2, 3, dev);
        assertSame(gm.popDeckDevelopmentCard(2,3), dev);
        assertThrows(IndexOutOfBoundsException.class, ()-> gm.pushDeckDevelopmentCard(4, 2, dev));


    }

    @Test
    void drawToken() throws IOException {
        GameMaster gmSp = new GameMaster("Single Player", 1);
        //non so come testarlo ancora
    }

    @Test
    void discardDevelopment() throws DeckDevelopmentCardException {
        gm.discardDevelopment(Color.GREEN, 4);
        assertEquals(gm.getDeckDevelopment().get(0).get(0).isEmpty(), true);

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