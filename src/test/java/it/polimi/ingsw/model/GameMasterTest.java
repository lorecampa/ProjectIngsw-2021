package it.polimi.ingsw.model;

import it.polimi.ingsw.client.data.ModelData;
import it.polimi.ingsw.exception.DeckDevelopmentCardException;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.token.CardToken;
import it.polimi.ingsw.model.token.PositionToken;
import it.polimi.ingsw.model.token.Token;
import it.polimi.ingsw.server.Match;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.VirtualClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
        gm.onPlayerStateChange(PlayerState.LEADER_MANAGE_AFTER);
        assertDoesNotThrow(()->gm.nextPlayer());
        gm.onPlayerStateChange(PlayerState.LEADER_MANAGE_AFTER);
        assertDoesNotThrow(()->gm.nextPlayer());
        gm.onPlayerStateChange(PlayerState.LEADER_MANAGE_AFTER);
        assertDoesNotThrow(()->gm.nextPlayer());
        gm.onPlayerStateChange(PlayerState.LEADER_MANAGE_AFTER);
        assertDoesNotThrow(()->gm.nextPlayer());


        gm.winningCondition();
        gm.onPlayerStateChange(PlayerState.LEADER_MANAGE_AFTER);
        assertDoesNotThrow(()->gm.nextPlayer());
        gm.onPlayerStateChange(PlayerState.LEADER_MANAGE_AFTER);
        assertDoesNotThrow(()->gm.nextPlayer());
        gm.onPlayerStateChange(PlayerState.LEADER_MANAGE_AFTER);
        assertDoesNotThrow(()->gm.nextPlayer());

    }

    @Test
    void getAndSetTest(){
        assertDoesNotThrow(()-> gm.setGameEnded(false));
        assertFalse(gm.isGameEnded());
        assertEquals(3,gm.getNumberOfPlayer());
        assertDoesNotThrow(() -> gm.getMarket());
        assertEquals("Lorenzo", gmSp.getCurrentPlayer());

        PersonalBoard lorenzoPersonalBoard = gmSp.getPlayerPersonalBoard("Lorenzo");
        assertEquals("Lorenzo", lorenzoPersonalBoard.getUsername());
        lorenzoPersonalBoard = gmSp.getCurrentPlayerPersonalBoard();
        assertEquals("Lorenzo", lorenzoPersonalBoard.getUsername());
        ModelData lorenzoModel = gmSp.getPlayerModelData("Lorenzo");
        assertEquals("Lorenzo", lorenzoModel.getUsername());

        assertEquals(3,gm.getAllPersonalBoard().size());

        assertDoesNotThrow(()->gm.getDeckDevelopmentCard(0,0));
        assertThrows(IndexOutOfBoundsException.class, () -> gm.getDeckDevelopmentCard(-1,0));

        assertNotNull(gm.toDeckDevData());
        assertNotNull(gm.toEffectDataBasePro());

        assertEquals(0, gmSp.getPlayerPosition("Lorenzo"));
        assertEquals("LorenzoIlMagnifico", GameMaster.getNameLorenzo());

        assertDoesNotThrow(()-> gm.restoreReferenceAfterServerQuit());

        assertNotNull(gm.getPlayerState());

        assertThrows(NullPointerException.class, ()->gm.attachPlayerVC(null));
        assertThrows(NullPointerException.class, ()->gm.attachLorenzoIlMagnificoVC(null));
    }

    @Test
    void drawToken(){
        assertEquals(0, gm.getSizeDeckToken());

        int sizeDeckToken = gmSp.getSizeDeckToken();
        assertEquals(7, sizeDeckToken);

        gmSp.onPlayerStateChange(PlayerState.LEADER_MANAGE_AFTER);
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
        assertThrows(DeckDevelopmentCardException.class, () -> gm.getDeckDevelopmentCard(0,0));

        assertDoesNotThrow(()->gm.discardDevelopmentSinglePlayer(Color.GREEN, 3));
        assertTrue(gm.getDeckDevelopment().get(0).get(0).isEmpty());

        assertDoesNotThrow(()->gm.discardDevelopmentSinglePlayer(Color.GREEN, 1));
        assertTrue(gm.getDeckDevelopment().get(0).get(0).isEmpty());

        assertDoesNotThrow(()->gm.discardDevelopmentSinglePlayer(Color.GREEN, 4));

    }

    @Test
    public void overrideTest(){
        assertDoesNotThrow(() -> gmSp.discardLeader());
        assertEquals(1, gmSp.getCurrentPlayerPersonalBoard().getFaithTrack().getCurrentPositionOnTrack());
        assertDoesNotThrow(()->gmSp.vaticanReportReached(0));
        assertDoesNotThrow(()->gmSp.discardResources(1));
        assertEquals(1, gmSp.getPlayerPersonalBoard(GameMaster.getNameLorenzo()).getFaithTrack().getCurrentPositionOnTrack());
        assertDoesNotThrow(()->gmSp.increasePlayerFaithPoint(1));
        assertEquals(2, gmSp.getCurrentPlayerPersonalBoard().getFaithTrack().getCurrentPositionOnTrack());
        assertDoesNotThrow(()->gmSp.onDeckDevelopmentCardRemove(1,1));
        assertDoesNotThrow(()->gmSp.onDeckDevelopmentCardRemove(-1,1));
        assertDoesNotThrow(()->gmSp.attachObserver(null));
        assertDoesNotThrow(()->gmSp.shuffleToken());
    }
}