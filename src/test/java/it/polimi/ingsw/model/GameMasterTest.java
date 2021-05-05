package it.polimi.ingsw.model;

import it.polimi.ingsw.exception.DeckDevelopmentCardException;
import it.polimi.ingsw.exception.JsonFileModificationError;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameMasterTest {

    GameMaster gm;
    GameMaster gmSinglePlayer;
    GameSetting gs;
    GameSetting gsSinglePlayer;

    int numOfPlayer;
    @BeforeEach
    void setUp() throws IOException, JsonFileModificationError {
        assertDoesNotThrow(()-> gsSinglePlayer = new GameSetting(1));
        gmSinglePlayer = new GameMaster(gsSinglePlayer, "Single Player");

        numOfPlayer = 4;
        assertDoesNotThrow(()-> gs = new GameSetting(numOfPlayer));
        gm = new GameMaster(gs, "player1");


        gm.setCurrentPlayer("player1");

        gm.addPlayer("player2");
        gm.addPlayer("player3");
        gm.addPlayer("player4");


        gm.getPlayerPersonalBoard("player1").getFaithTrack().attachGameMasterObserver(gm);
        gm.getPlayerPersonalBoard("player2").getFaithTrack().attachGameMasterObserver(gm);
        gm.getPlayerPersonalBoard("player3").getFaithTrack().attachGameMasterObserver(gm);
        gm.getPlayerPersonalBoard("player4").getFaithTrack().attachGameMasterObserver(gm);


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
        assertDoesNotThrow(()->gm.deliverLeaderCards());
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
        assertEquals(0, gm.getPlayerPersonalBoard("player1").getFaithTrack().getVictoryPoints());
        assertEquals(2, gm.getPlayerPersonalBoard("player2").getFaithTrack().getVictoryPoints());
        assertEquals(2, gm.getPlayerPersonalBoard("player3").getFaithTrack().getVictoryPoints());
        assertEquals(2, gm.getPlayerPersonalBoard("player4").getFaithTrack().getVictoryPoints());
    }

    @Test
    void updateFromCardManager() {
        gm.discardLeader();
        assertEquals(1, gm.getPlayerPersonalBoard("player1").getFaithTrack().getCurrentPositionOnTrack());
        gm.getPlayerPersonalBoard("player1").getFaithTrack().movePlayer(4);
        gm.discardLeader();
        assertEquals(2, gm.getPlayerPersonalBoard("player1").getFaithTrack().getVictoryPoints());
    }


}