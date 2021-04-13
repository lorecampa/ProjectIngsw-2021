package it.polimi.ingsw.model.card.Effect.Activation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.card.Effect.State;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MarbleEffectTest {
    Market market;
    ArrayList<Resource> trasformIn = new ArrayList<>();
    Effect me1;
    Resource res = ResourceFactory.createResource(ResourceType.COIN, 2);
    private final static int ROW_SIZE = 4;
    @BeforeEach
    void init(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        assertDoesNotThrow(() -> market = mapper.readValue(new File("src/main/resources/json/market.json"), Market.class));
        trasformIn.add(res);



    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2})
    void doEffect(int index) {
        market.reset();
        market.insertMarbleInRow(index);

        me1 = new MarbleEffect(trasformIn);
        me1.attachMarket(market);
        assertDoesNotThrow(()->me1.doEffect(State.MARKET_STATE));

        int whiteMarble = market.getWhiteMarbleDrew();
        //real resources extracted
        int numResourceToSend = 0;
        for (Resource res: market.getResourceToSend()){
            numResourceToSend += res.getValue();
        }
        //resources that we suppose to have
        int resourceNumExpected;
        if(whiteMarble != 0){
            resourceNumExpected = whiteMarble * res.getValue() + (ROW_SIZE-whiteMarble);
        }else{
            resourceNumExpected = ROW_SIZE;
        }

        assertEquals(numResourceToSend , resourceNumExpected);

    }
}