package it.polimi.ingsw.model.personalBoard.market;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class MarbleTest {

    Market market;
    Marble blueMarble;
    Marble greyMarble;
    Marble purpleMarble;
    Marble redMarble;
    Marble whiteMarble;
    Marble yellowMarble;


    @BeforeEach
    void init() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        assertDoesNotThrow(() -> market = mapper.readValue(new File("src/main/resources/json/market.json"), Market.class));
        blueMarble = new BlueMarble();
        greyMarble = new GreyMarble();
        purpleMarble = new PurpleMarble();
        redMarble = new RedMarble();
        whiteMarble = new WhiteMarble();
        yellowMarble = new YellowMarble();
    }

    @Test
    void doMarbleAction_Blue() throws NegativeResourceException {
        blueMarble.doMarbleAction(market);
        assertEquals(1, market.getResourceToSend().size());
        assertEquals(1, market.getResourceToSend().get(0).getValue());
        assertEquals(ResourceType.SHIELD, market.getResourceToSend().get(0).getType());
    }

    @Test
    void doMarbleAction_Grey() throws NegativeResourceException {
        greyMarble.doMarbleAction(market);
        assertEquals(1, market.getResourceToSend().size());
        assertEquals(1, market.getResourceToSend().get(0).getValue());
        assertEquals(ResourceType.STONE, market.getResourceToSend().get(0).getType());
    }

    @Test
    void doMarbleAction_Purple() throws NegativeResourceException {
        purpleMarble.doMarbleAction(market);
        assertEquals(1, market.getResourceToSend().size());
        assertEquals(1, market.getResourceToSend().get(0).getValue());
        assertEquals(ResourceType.SERVANT, market.getResourceToSend().get(0).getType());
    }

    @Test
    void doMarbleAction_Red() throws NegativeResourceException {
        redMarble.doMarbleAction(market);
        assertEquals(1, market.getResourceToSend().size());
        assertEquals(1, market.getResourceToSend().get(0).getValue());
        assertEquals(ResourceType.FAITH, market.getResourceToSend().get(0).getType());
    }

    @Test
    void doMarbleAction_White() throws NegativeResourceException {
        whiteMarble.doMarbleAction(market);
        assertEquals(1, market.getWhiteMarbleDrew());
    }

    @Test
    void doMarbleAction_Yellow() throws NegativeResourceException {
        yellowMarble.doMarbleAction(market);
        assertEquals(1, market.getResourceToSend().size());
        assertEquals(1, market.getResourceToSend().get(0).getValue());
        assertEquals(ResourceType.COIN, market.getResourceToSend().get(0).getType());
    }
}