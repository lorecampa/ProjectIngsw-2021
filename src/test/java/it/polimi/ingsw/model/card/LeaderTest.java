package it.polimi.ingsw.model.card;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.GameSetting;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LeaderTest {
    GameSetting gs;
    ResourceManager rm;
    Market mk;
    CardManager cm;
    ArrayList<Leader> leaders;
    Leader discountLeader;
    Leader warehouseLeader;
    Leader marbleLeader;
    Leader productionLeader;

    @BeforeEach
    void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        assertDoesNotThrow(()-> gs = new GameSetting(4));
        cm = new CardManager(gs.getBaseProduction());

        //strongbox (5 coin, 2 shield, 4 servant, 0 stone)
        //warehouse (0 -> , 1-> 2 stone, 2-> )
        rm = new ResourceManager();
        Resource res1 = ResourceFactory.createResource(ResourceType.COIN, 5);
        Resource res2 = ResourceFactory.createResource(ResourceType.SHIELD, 2);
        Resource res3 = ResourceFactory.createResource(ResourceType.SERVANT, 4);
        rm.addToStrongbox(res1);
        rm.addToStrongbox(res2);
        rm.addToStrongbox(res3);

        Resource res4 = ResourceFactory.createResource(ResourceType.STONE, 2);
        assertDoesNotThrow(()->rm.addToWarehouse(true, 1, res4));



        mk = mapper.readValue(new File("src/main/resources/json/market.json"), Market.class);

        leaders =
                mapper.readValue(new File("src/main/resources/json/leader.json"), new TypeReference<>() {
                });
        FaithTrack faithTrack =
                mapper.readValue(new File("src/main/resources/json/faithTrack.json"), FaithTrack.class);
        PersonalBoard pb = new PersonalBoard("lupo", faithTrack, rm, cm);

        //cardReq (green 1, level 1) (yellow 1, level 1)
        //(servant 1)
        discountLeader = leaders.get(0);

        discountLeader.attachCardToUser(pb, mk);

        //resReq (servant 5)
        //(shield 2)
        warehouseLeader = leaders.get(6);


        warehouseLeader.attachCardToUser(pb, mk);


        //cardReq (blue 2, level 1) (yellow 1, level 1)
        //(stone 1)
        marbleLeader = leaders.get(10);


        marbleLeader.attachCardToUser(pb, mk);


        //cardReq (green 1, level 2)
        //(coin 1) -> (any 1, faith 1)
        productionLeader = leaders.get(15);



        productionLeader.attachCardToUser(pb, mk);

        assertEquals(16, leaders.size());

        ArrayList<Development> developmentsJson =
                mapper.readValue(new File("src/main/resources/json/development.json"), new TypeReference<ArrayList<Development>>() {});

        //(slot 1) -> 1: green 2: blue
        assertDoesNotThrow(()->cm.addDevelopmentCardTo(developmentsJson.get(0), 0));
        cm.emptyCardSlotBuffer();

        assertDoesNotThrow(()->cm.addDevelopmentCardTo(developmentsJson.get(30), 0));
        cm.emptyCardSlotBuffer();

        //(slot 2) -> 1: blue
        assertDoesNotThrow(()->cm.addDevelopmentCardTo(developmentsJson.get(10), 1));
        cm.emptyCardSlotBuffer();

        //(slot 3) -> 1: yellow
        assertDoesNotThrow(()->cm.addDevelopmentCardTo(developmentsJson.get(15), 2));
        cm.emptyCardSlotBuffer();

    }

    @Test
    void checkRequirements() {
        rm.newTurn();
        assertFalse(warehouseLeader.checkRequirements());
        rm.newTurn();
        assertTrue(discountLeader.checkRequirements());
        rm.newTurn();
        assertFalse(marbleLeader.checkRequirements());
        rm.newTurn();
        assertFalse(productionLeader.checkRequirements());

    }

}