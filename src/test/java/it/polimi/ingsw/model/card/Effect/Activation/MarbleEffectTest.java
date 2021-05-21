package it.polimi.ingsw.model.card.Effect.Activation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.client.data.ColorData;
import it.polimi.ingsw.client.data.EffectData;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.TurnState;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MarbleEffectTest {
    private static Market market;

    private static final ArrayList<Resource> trasformIn1 = new ArrayList<>();
    private static final ArrayList<Resource> trasformIn2 = new ArrayList<>();
    private static final ArrayList<Resource> trasformIn3 = new ArrayList<>();

    private static Effect me1;
    private static Effect me2;
    private static Effect me3;

    private final static int ROW_SIZE = 3;
    private final static int COLUMN_SIZE = 4;


    @BeforeAll
    public static void  init(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        assertDoesNotThrow(() -> market = mapper.readValue(new File("src/main/resources/json/market.json"), Market.class));

        Resource res1 = ResourceFactory.createResource(ResourceType.COIN, 2);
        Resource res2 = ResourceFactory.createResource(ResourceType.SERVANT, 3);
        Resource res3 = ResourceFactory.createResource(ResourceType.SHIELD, 5);

        trasformIn1.add(res1);
        trasformIn2.add(res2);
        trasformIn2.add(res3);


        //create first marble effect
        me1 = new MarbleEffect(trasformIn1);
        me1.attachMarket(market);
        //create second marble effect
        me2 = new MarbleEffect(trasformIn2);
        me2.attachMarket(market);
        //create third marble effect
        me3 = new MarbleEffect(trasformIn3);
        me3.attachMarket(market);

    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,ROW_SIZE-1})
    void doEffectRowOneResource(int index) {
        //for each row insert a marble in the market and then reset it

        market.reset();
        market.insertMarbleInRow(index);
        market.setWhiteMarbleToTransform(market.getWhiteMarbleDrew());
        assertDoesNotThrow(()->me1.doEffect(TurnState.WHITE_MARBLE_CONVERSION));

        int whiteMarble = market.getWhiteMarbleToTransform();

        //real resources extracted
        int numResourceToSend = 0;
        for (Resource res: market.getResourceToSend()){
            numResourceToSend += res.getValue();
        }
        //resources that we suppose to have
        int resourceNumExpected = 0;
        for (Resource resTransform: trasformIn1){
            resourceNumExpected += whiteMarble*resTransform.getValue();
        }
        resourceNumExpected += COLUMN_SIZE - whiteMarble;


        assertEquals(numResourceToSend , resourceNumExpected);

    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,COLUMN_SIZE-1})
    void doEffectColumnOneResource(int index) {
        //for each column insert a marble in the market and then reset it
        market.reset();
        market.insertMarbleInCol(index);
        market.setWhiteMarbleToTransform(market.getWhiteMarbleDrew());
        assertDoesNotThrow(()->me1.doEffect(TurnState.WHITE_MARBLE_CONVERSION));

        int whiteMarble = market.getWhiteMarbleToTransform();

        //real resources extracted
        int numResourceToSend = 0;
        for (Resource res: market.getResourceToSend()){
            numResourceToSend += res.getValue();
        }
        //resources that we suppose to have
        int resourceNumExpected = 0;
        for (Resource resTransform: trasformIn1){
            resourceNumExpected += whiteMarble*resTransform.getValue();
        }
        resourceNumExpected += ROW_SIZE - whiteMarble;


        assertEquals(numResourceToSend , resourceNumExpected);

    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,COLUMN_SIZE-1})
    void insertTwoResourceColumn(int index) {
        //for each column insert a marble in the market and then reset it
        market.reset();
        market.insertMarbleInCol(index);
        market.setWhiteMarbleToTransform(market.getWhiteMarbleDrew());

        assertDoesNotThrow(()->me2.doEffect(TurnState.WHITE_MARBLE_CONVERSION));

        int whiteMarble = market.getWhiteMarbleToTransform();

        //real resources extracted
        int numResourceToSend = 0;
        for (Resource res: market.getResourceToSend()){
            numResourceToSend += res.getValue();
        }
        //resources that we suppose to have
        int resourceNumExpected = 0;
        for (Resource resTransform: trasformIn2){
            resourceNumExpected += whiteMarble*resTransform.getValue();
        }
        resourceNumExpected += ROW_SIZE - whiteMarble;


        assertEquals(numResourceToSend , resourceNumExpected);

    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,ROW_SIZE-1})
    void EmptyEffect(int index) {
        //for each row insert a marble in the market and then reset it
        market.reset();
        market.insertMarbleInRow(index);
        market.setWhiteMarbleToTransform(market.getWhiteMarbleDrew());

        assertDoesNotThrow(()->me3.doEffect(TurnState.WHITE_MARBLE_CONVERSION));

        int whiteMarble = market.getWhiteMarbleToTransform();

        //real resources extracted
        int numResourceToSend = 0;
        for (Resource res: market.getResourceToSend()){
            numResourceToSend += res.getValue();
        }
        //resources that we suppose to have
        int resourceNumExpected = 0;
        for (Resource resTransform: trasformIn3){
            resourceNumExpected += whiteMarble*resTransform.getValue();
        }
        resourceNumExpected += COLUMN_SIZE - whiteMarble;


        assertEquals(numResourceToSend , resourceNumExpected);

    }

    @Test
    void toDataTest(){
        ArrayList<Resource> cost = new ArrayList<>();
        cost.add(ResourceFactory.createResource(ResourceType.SHIELD, 1));

        MarbleEffect myEffect= new MarbleEffect(cost);
        EffectData effectData= myEffect.toEffectData();

        assertEquals("Marble effect: ", effectData.getDescriptions());
    }


}