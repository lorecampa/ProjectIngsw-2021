package it.polimi.ingsw.model.personalBoard;

import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.GameSetting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PersonalBoardTest {

    PersonalBoard personalBoard;
    GameSetting gameSetting;
    GameMaster gameMaster;

    @BeforeEach
    public void init(){
        assertDoesNotThrow(()-> gameSetting = new GameSetting(1));
        assertDoesNotThrow(()-> gameMaster = new GameMaster(gameSetting,new ArrayList<>(){{add("Davide");}}));
        personalBoard = gameMaster.getPlayerPersonalBoard("Davide");
    }

    @Test
    void isInkwell() {
        assertTrue(personalBoard.isInkwell());
    }

    @Test
    void attachGameMasterObserver() {
        assertDoesNotThrow(()-> personalBoard.attachGameMasterObserver(gameMaster));
    }

    @Test
    void attachVirtualClient() {
        assertDoesNotThrow(()-> personalBoard.attachVirtualClient(null));
    }

    @Test
    void toClient() {
        assertNotNull(personalBoard.toClient(true));
        assertNotNull(personalBoard.toClient(false));
    }

    @Test
    void getFaithTrack() {
        assertNotNull(personalBoard.getFaithTrack());
    }

    @Test
    void getCardManager() {
        assertNotNull(personalBoard.getCardManager());
    }

    @Test
    void getResourceManager() {
        assertNotNull(personalBoard.getResourceManager());
    }

    @Test
    void setInkwell() {
        personalBoard.setInkwell(false);
        assertFalse(personalBoard.isInkwell());
    }

    @Test
    void getUsername() {
        assertEquals("Davide", personalBoard.getUsername());
    }

}