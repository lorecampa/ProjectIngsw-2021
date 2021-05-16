package it.polimi.ingsw.model.card.requirement;

import it.polimi.ingsw.exception.NotEnoughRequirementException;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class ResourceReqTest {
    ResourceManager rm;
    @BeforeEach
    void init(){
        rm = new ResourceManager();
        //(7 coin, 2 shield, 3 servant)
        rm.addToStrongbox(ResourceFactory.createResource(ResourceType.COIN, 5));
        rm.addToStrongbox(ResourceFactory.createResource(ResourceType.SHIELD, 2));
        assertDoesNotThrow(()->rm.addToWarehouse(true, 1, ResourceFactory.createResource(ResourceType.COIN, 2)));
        assertDoesNotThrow(()->rm.addToWarehouse(true, 2, ResourceFactory.createResource(ResourceType.SERVANT, 3)));
        rm.restoreRM();
    }
    @Test
    void Normal() {
        ArrayList<Resource> resourceReq = new ArrayList<>();
        // (2 coin, 2 shield) -> true
        resourceReq.add(ResourceFactory.createResource(ResourceType.COIN, 2));
        resourceReq.add(ResourceFactory.createResource(ResourceType.SHIELD, 2));

        Requirement req = new ResourceReq(resourceReq);
        req.attachResourceManager(rm);
        assertDoesNotThrow(()->req.checkRequirement(true));

    }

    @Test
    void MoreResource() {
        //(5 coin , 2 shield, 1 stone) -> false
        ArrayList<Resource> resourceReq = new ArrayList<>();
        resourceReq.add(ResourceFactory.createResource(ResourceType.COIN, 5));
        resourceReq.add(ResourceFactory.createResource(ResourceType.SHIELD, 2));
        resourceReq.add(ResourceFactory.createResource(ResourceType.STONE, 1));


        Requirement req = new ResourceReq(resourceReq);
        req.attachResourceManager(rm);
        assertThrows(NotEnoughRequirementException.class, ()->req.checkRequirement(true));

    }

    @Test
    void AnyRequeriment() {
        //(7 coin , 2 servant, 3 any) -> true
        ArrayList<Resource> resourceReq = new ArrayList<>();
        resourceReq.add(ResourceFactory.createResource(ResourceType.COIN, 7));
        resourceReq.add(ResourceFactory.createResource(ResourceType.SERVANT, 2));
        resourceReq.add(ResourceFactory.createResource(ResourceType.ANY, 3));

        Requirement req = new ResourceReq(resourceReq);
        req.attachResourceManager(rm);
        assertDoesNotThrow(()->req.checkRequirement(true));

    }

    @Test
    void ThreeRequirement() {
        //(3 coin , 3 servant, 2 any) -> true
        ArrayList<Resource> resourceReq1 = new ArrayList<>();
        resourceReq1.add(ResourceFactory.createResource(ResourceType.COIN, 3));
        resourceReq1.add(ResourceFactory.createResource(ResourceType.SERVANT, 3));
        resourceReq1.add(ResourceFactory.createResource(ResourceType.ANY, 2));

        //(2 coin , 2 any) -> true
        ArrayList<Resource> resourceReq2 = new ArrayList<>();
        resourceReq2.add(ResourceFactory.createResource(ResourceType.COIN, 2));
        resourceReq2.add(ResourceFactory.createResource(ResourceType.ANY, 2));

        //(1 any) -> false
        ArrayList<Resource> resourceReq3 = new ArrayList<>();
        resourceReq3.add(ResourceFactory.createResource(ResourceType.ANY, 1));

        Requirement req1 = new ResourceReq(resourceReq1);
        req1.attachResourceManager(rm);
        Requirement req2 = new ResourceReq(resourceReq2);
        req2.attachResourceManager(rm);
        Requirement req3 = new ResourceReq(resourceReq3);
        req3.attachResourceManager(rm);

        assertDoesNotThrow(()->req1.checkRequirement(true));
        assertDoesNotThrow(()->req2.checkRequirement(true));
        assertThrows(NotEnoughRequirementException.class, ()->req3.checkRequirement(true));
    }




}