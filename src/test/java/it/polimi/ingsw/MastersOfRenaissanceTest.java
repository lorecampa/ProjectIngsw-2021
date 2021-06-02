package it.polimi.ingsw;

import it.polimi.ingsw.model.GameSetting;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class MastersOfRenaissanceTest
{

    private GameSetting gs;
    private GameSetting gsSinglePlayer;
    @BeforeEach
    void init(){
        assertDoesNotThrow(()-> gs = new GameSetting(4));
        assertDoesNotThrow(()-> gsSinglePlayer = new GameSetting(1));

    }
}
