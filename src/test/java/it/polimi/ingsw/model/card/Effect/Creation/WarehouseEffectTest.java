package it.polimi.ingsw.model.card.Effect.Creation;

import it.polimi.ingsw.exception.TooMuchResourceDepotException;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.controller.TurnState;
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
        assertDoesNotThrow(()->effect.doEffect(TurnState.LEADER_MANAGE_BEFORE));

        //add resources to our leaders depots just created
        assertDoesNotThrow(()->rm.addToWarehouse(false, 0, ResourceFactory.createResource(ResourceType.COIN, 2)));
        assertDoesNotThrow(()->rm.addToWarehouse(false, 1, ResourceFactory.createResource(ResourceType.SHIELD, 3)));
        assertDoesNotThrow(()->rm.addToWarehouse(false, 2, ResourceFactory.createResource(ResourceType.SERVANT, 1)));

        assertThrows(TooMuchResourceDepotException.class, ()->rm.addToWarehouse(false, 0, ResourceFactory.createResource(ResourceType.COIN, 1)));
        assertThrows(TooMuchResourceDepotException.class, ()->rm.addToWarehouse(false, 1, ResourceFactory.createResource(ResourceType.SHIELD, 1)));
        assertThrows(TooMuchResourceDepotException.class, ()->rm.addToWarehouse(false, 2, ResourceFactory.createResource(ResourceType.SERVANT, 1)));

        assertDoesNotThrow(()->rm.subToWarehouse(false, 0, ResourceFactory.createResource(ResourceType.COIN, 1)));
        assertDoesNotThrow(()->rm.addToWarehouse(false, 0, ResourceFactory.createResource(ResourceType.COIN, 1)));


    }
}