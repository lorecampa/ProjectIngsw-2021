package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ResourceManagerTest {

    ResourceManager rs = new ResourceManager();

    @BeforeEach
    void setUp(){
        rs.addToStrongbox(ResourceFactory.createResource(ResourceType.COIN,5));
        rs.addToStrongbox(ResourceFactory.createResource(ResourceType.SERVANT,3));
        rs.addToStrongbox(ResourceFactory.createResource(ResourceType.STONE,2));

        assertDoesNotThrow(() -> rs.addToWarehouse(true,0,ResourceFactory.createResource(ResourceType.COIN,1)));
        assertDoesNotThrow(() -> rs.addToWarehouse(true,1,ResourceFactory.createResource(ResourceType.SHIELD,2)));
    }

    @Test
    void canIAfford() {
        rs.newTurn();
        rs.addDiscount(ResourceFactory.createResource(ResourceType.COIN,1));
        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(ResourceFactory.createResource(ResourceType.COIN,7));
        resources.add(ResourceFactory.createResource(ResourceType.SERVANT,3));
        resources.add(ResourceFactory.createResource(ResourceType.STONE,1));

        assertTrue(rs.canIAfford(resources,true));

        resources.clear();
        resources.add(ResourceFactory.createResource(ResourceType.STONE,1));
        resources.add(ResourceFactory.createResource(ResourceType.ANY,2));

        assertTrue(rs.canIAfford(resources,false));
    }
}