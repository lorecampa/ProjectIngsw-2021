package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.ClientGUI;
import it.polimi.ingsw.client.data.MarketData;
import it.polimi.ingsw.model.GameSetting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MarketControllerTest {

    private GameSetting gameSetting;

    @BeforeEach
    void setUp() {
        assertDoesNotThrow(() -> gameSetting = new GameSetting(1));
        MarketData marketData = gameSetting.getMarket().toMarketData();
        Client.getInstance().setMyName("davide");
        ArrayList<String> usernames = new ArrayList<>();
        usernames.add("davide");
        Client.getInstance().setModels(usernames);
        Client.getInstance().setMarketData(marketData);
    }

    @Test
    void testMarket() {
        ClientGUI.main(null);
    }
}