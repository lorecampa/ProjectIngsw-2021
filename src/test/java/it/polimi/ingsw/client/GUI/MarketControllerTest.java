package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.data.MarketData;
import it.polimi.ingsw.model.GameSetting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarketControllerTest {

    private GameSetting gameSetting;

    @BeforeEach
    void setUp() {
        assertDoesNotThrow(() -> gameSetting = new GameSetting(1));
        MarketData marketData = gameSetting.getMarket().toMarketData();

        Client.getInstance();
        Client.getInstance().setMarketData(marketData);
        ClientGUI.main(new String[0]);
    }

    @Test
    void testMarket() {
        ClientGUI.main(new String[0]);
    }
}