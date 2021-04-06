package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameMasterTest {

    GameMaster gameMaster;
    @BeforeEach
    void setUp() throws IOException {
        gameMaster = new GameMaster();
        gameMaster.setCurrentPlayer("player1");

        gameMaster.addPlayer("player1");
        gameMaster.addPlayer("player2");
        gameMaster.addPlayer("player3");
        gameMaster.addPlayer("player4");

        gameMaster.getPlayerPersonalBoard("player1").getFaithTrack().attachObserver(gameMaster);
        gameMaster.getPlayerPersonalBoard("player2").getFaithTrack().attachObserver(gameMaster);
        gameMaster.getPlayerPersonalBoard("player3").getFaithTrack().attachObserver(gameMaster);
        gameMaster.getPlayerPersonalBoard("player4").getFaithTrack().attachObserver(gameMaster);

    }

    @Test
    void updateFromFaithTrack() {
        gameMaster.getPlayerPersonalBoard("player4").getFaithTrack().movePlayer(5);
        gameMaster.getPlayerPersonalBoard("player3").getFaithTrack().movePlayer(6);
        gameMaster.getPlayerPersonalBoard("player2").getFaithTrack().movePlayer(7);
        gameMaster.getPlayerPersonalBoard("player1").getFaithTrack().movePlayer(8);

        assertEquals(2,gameMaster.getPlayerPersonalBoard("player1").getFaithTrack().getPopeFavorVP());
        assertEquals(2,gameMaster.getPlayerPersonalBoard("player2").getFaithTrack().getPopeFavorVP());
        assertEquals(2,gameMaster.getPlayerPersonalBoard("player3").getFaithTrack().getPopeFavorVP());
        assertEquals(2,gameMaster.getPlayerPersonalBoard("player4").getFaithTrack().getPopeFavorVP());
    }

    @Test
    void updateFromResourceManager() {
        gameMaster.updateFromResourceManager(6);
        assertEquals(0,gameMaster.getPlayerPersonalBoard("player1").getFaithTrack().getVictoryPoints());
        assertEquals(2,gameMaster.getPlayerPersonalBoard("player2").getFaithTrack().getVictoryPoints());
        assertEquals(2,gameMaster.getPlayerPersonalBoard("player3").getFaithTrack().getVictoryPoints());
        assertEquals(2,gameMaster.getPlayerPersonalBoard("player4").getFaithTrack().getVictoryPoints());
    }

    @Test
    void updateFromCardManager() {
        gameMaster.updateFromCardManager();
        assertEquals(1,gameMaster.getPlayerPersonalBoard("player1").getFaithTrack().getCurrentPositionOnTrack());
        gameMaster.getPlayerPersonalBoard("player1").getFaithTrack().movePlayer(4);
        gameMaster.updateFromCardManager();
        assertEquals(2,gameMaster.getPlayerPersonalBoard("player1").getFaithTrack().getVictoryPoints());
    }
}