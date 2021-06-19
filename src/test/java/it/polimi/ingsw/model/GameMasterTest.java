package it.polimi.ingsw.model;

import it.polimi.ingsw.client.data.ModelData;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.token.CardToken;
import it.polimi.ingsw.model.token.PositionToken;
import it.polimi.ingsw.model.token.Token;
import it.polimi.ingsw.server.VirtualClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GameMasterTest {

    GameMaster gm;
    GameMaster gm1;
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
        assertDoesNotThrow(()-> gm1 = new GameMaster());
    }



    @Test
    void deliverLeaderCard() {
        assertEquals(16, gm.getSizeDeckLeader());
        assertEquals(3, gm.getNumActivePlayers());
        assertDoesNotThrow(()->gm.deliverLeaderCards());
        assertEquals(4, gm.getSizeDeckLeader());
    }

    @Test
    void nextPlayerTest(){
        assertDoesNotThrow(()->gm.nextPlayer());
        assertDoesNotThrow(()->gm.nextPlayer());
        assertDoesNotThrow(()->gm.nextPlayer());
        assertDoesNotThrow(()->gm.nextPlayer());

        gm.winningCondition();

        assertDoesNotThrow(()->gm.nextPlayer());
        assertDoesNotThrow(()->gm.nextPlayer());
        assertDoesNotThrow(()->gm.nextPlayer());

    }

    @Test
    void getAndSetTest(){
        assertDoesNotThrow(()-> gm.setGameEnded(true));
        assertEquals(3,gm.getNumberOfPlayer());
        assertDoesNotThrow(() -> gm.getMarket());
        assertEquals("Lorenzo", gmSp.getCurrentPlayer());

        PersonalBoard lorenzoPersonalBoard = gmSp.getPlayerPersonalBoard("Lorenzo");
        assertEquals("Lorenzo", lorenzoPersonalBoard.getUsername());
        lorenzoPersonalBoard = gmSp.getCurrentPlayerPersonalBoard();
        assertEquals("Lorenzo", lorenzoPersonalBoard.getUsername());
        ModelData lorenzoModel = gmSp.getPlayerModelData("Lorenzo");
        assertEquals("Lorenzo", lorenzoModel.getUsername());

    }

    @Test
    void drawToken(){
        assertEquals(0, gm.getSizeDeckToken());

        int sizeDeckToken = gmSp.getSizeDeckToken();
        assertEquals(7, sizeDeckToken);

        assertDoesNotThrow(()-> gmSp.nextPlayer());
        assertEquals(sizeDeckToken, gmSp.getSizeDeckToken());
    }

    @Test
    void tokenTest(){
        int sizeDeckToken = gmSp.getSizeDeckToken();
        Token cardToken = new CardToken(Color.BLUE,1);
        Token positionToken = new PositionToken(3,false);
        assertDoesNotThrow(()-> positionToken.doActionToken(gmSp));
        assertEquals(sizeDeckToken, gmSp.getSizeDeckToken());
        assertDoesNotThrow(()-> cardToken.doActionToken(gmSp));
        assertEquals(sizeDeckToken, gmSp.getSizeDeckToken());

        assertEquals("\n" +
                "CardToken\n" +
                "cardColor= BLUE\n" +
                "numDiscard=1",cardToken.toString());

        assertEquals("\n" +
                "PositionToken\n" +
                "position= 3\n" +
                "isShuffle= false", positionToken.toString());
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