package it.polimi.ingsw.model.card.Effect.Creation;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.client.data.EffectData;
import it.polimi.ingsw.client.data.EffectType;
import it.polimi.ingsw.exception.TooMuchResourceDepotException;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.PlayerState;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseEffectTest {
    ResourceManager rm;
    Effect effect;
    ArrayList<Resource> resources = new ArrayList<>();

    @BeforeEach
    void init(){
        rm = new ResourceManager();
    }

    @Test
    void doEffect() {
        Resource res1 = ResourceFactory.createResource(ResourceType.COIN, 2);
        Resource res2 = ResourceFactory.createResource(ResourceType.SHIELD, 3);
        Resource res3 = ResourceFactory.createResource(ResourceType.SERVANT, 1);
        resources.add(res1);
        resources.add(res2);
        resources.add(res3);

        effect = new WarehouseEffect(resources);
        effect.attachResourceManager(rm);
        assertDoesNotThrow(()->effect.doEffect(PlayerState.LEADER_MANAGE_BEFORE));

        //add resources to our leaders depots just created
        assertDoesNotThrow(()->rm.addToWarehouse(false, 0, ResourceFactory.createResource(ResourceType.COIN, 2)));
        assertDoesNotThrow(()->rm.addToWarehouse(false, 1, ResourceFactory.createResource(ResourceType.SHIELD, 3)));
        assertDoesNotThrow(()->rm.addToWarehouse(false, 2, ResourceFactory.createResource(ResourceType.SERVANT, 1)));

        assertThrows(TooMuchResourceDepotException.class, ()->rm.addToWarehouse(false, 0, ResourceFactory.createResource(ResourceType.COIN, 1)));
        assertThrows(TooMuchResourceDepotException.class, ()->rm.addToWarehouse(false, 1, ResourceFactory.createResource(ResourceType.SHIELD, 1)));
        assertThrows(TooMuchResourceDepotException.class, ()->rm.addToWarehouse(false, 2, ResourceFactory.createResource(ResourceType.SERVANT, 1)));

        assertDoesNotThrow(()->rm.subToWarehouse(false, 0, ResourceFactory.createResource(ResourceType.COIN, 1)));
        assertDoesNotThrow(()->rm.addToWarehouse(false, 0, ResourceFactory.createResource(ResourceType.COIN, 1)));

        assertDoesNotThrow(()->effect.attachMarket(null));
    }

    private String effectSer;
    private EffectData effectData;
    @Test
    void toDataTest(){
        ArrayList<Resource> depot = new ArrayList<>();
        depot.add(ResourceFactory.createResource(ResourceType.SHIELD, 1));
        WarehouseEffect myEffect= new WarehouseEffect(depot);
        effectData= myEffect.toEffectData();

        assertEquals("Warehouse effect: ", effectData.getDescriptions());
        assertEquals(EffectType.WAREHOUSE, effectData.getType());

        ObjectMapper mapper = new ObjectMapper();

        assertDoesNotThrow(()->effectSer = mapper.writeValueAsString(effectData));
        assertDoesNotThrow(()-> effectData = mapper.readValue(effectSer,EffectData.class));
    }

    @Test
    void discardEffect(){
        ArrayList<Resource> depot = new ArrayList<>();
        depot.add(ResourceFactory.createResource(ResourceType.SHIELD, 1));
        WarehouseEffect myEffect= new WarehouseEffect(depot);
        myEffect.attachResourceManager(rm);
        assertDoesNotThrow(()->myEffect.doEffect(PlayerState.LEADER_MANAGE_BEFORE));
    }
}