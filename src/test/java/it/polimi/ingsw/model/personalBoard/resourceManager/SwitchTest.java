package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SwitchTest {
    ResourceManager rm;
    @BeforeEach
    void init(){
       rm = new ResourceManager();
       assertDoesNotThrow(()->rm.addToWarehouse(true, 0,
               ResourceFactory.createResource(ResourceType.COIN, 1)));
        assertDoesNotThrow(()->rm.addToWarehouse(true, 2,
                ResourceFactory.createResource(ResourceType.SHIELD, 3)));
    }


    @Test
    void switchResourceFromDepotToDepot() {
        assertDoesNotThrow(()->rm.switchResourceFromDepotToDepot(0, true, 1, true));
        assertDoesNotThrow(() -> rm.switchResourceFromDepotToDepot(0, true, 1, true));
    }
}