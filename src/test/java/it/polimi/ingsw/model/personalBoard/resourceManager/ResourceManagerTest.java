package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.exception.InvalidOrganizationWarehouseException;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.exception.TooMuchResourceDepotException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ResourceManagerTest {

    ResourceManager rs = new ResourceManager();
    @BeforeEach
    void Init(){
        rs.addToStrongbox(ResourceFactory.createResource(ResourceType.COIN,5));
        rs.addToStrongbox(ResourceFactory.createResource(ResourceType.SERVANT,3));
        rs.addToStrongbox(ResourceFactory.createResource(ResourceType.STONE,2));

        assertDoesNotThrow(() -> rs.addToWarehouse(true,0,ResourceFactory.createResource(ResourceType.COIN,1)));
        assertDoesNotThrow(() -> rs.addToWarehouse(true,1,ResourceFactory.createResource(ResourceType.SHIELD,1)));
    }

    @Test
    void newTurn() {
        //No idea how to test it
    }

    @Test
    void resourceFromMarket() {
        //No idea how to test it
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void addToWarehouse(int index) {
        switch (index){
            case 0:
                //TooMuchResourceDepotException
                Resource resourceTooBig= ResourceFactory.createResource(ResourceType.SHIELD, 4);
                assertThrows(TooMuchResourceDepotException.class, ()->rs.addToWarehouse(true, 1,resourceTooBig));
                break;
            case 1:
                //InvalidOrganizationWarehouseException
                assertThrows(InvalidOrganizationWarehouseException.class, ()->rs.addToWarehouse(true, 2,ResourceFactory.createResource(ResourceType.COIN, 1)));
                break;
            case 2:
                assertDoesNotThrow(() -> rs.addToWarehouse(true,1,ResourceFactory.createResource(ResourceType.SHIELD,1)));
                assertDoesNotThrow(() -> rs.addToWarehouse(true,2,ResourceFactory.createResource(ResourceType.SERVANT,2)));
                Resource r1=ResourceFactory.createResource(ResourceType.SERVANT, 2);
                rs.addLeaderDepot(new Depot(r1, true, 10));
                assertDoesNotThrow(() -> rs.addToWarehouse(false,0,ResourceFactory.createResource(ResourceType.SERVANT,2)));
                break;
        }
    }

    @Test
    void doProduction() {
        ArrayList<Resource> arrRes = new ArrayList<>();
        arrRes.add(ResourceFactory.createResource(ResourceType.COIN, 5));
        rs.addToResourcesToProduce(arrRes, true, true);
        rs.doProduction();
        assertEquals(10,  rs.getStrongbox().howManyDoIHave(ResourceType.COIN));
    }

    @Test
    void addToStrongbox() {
        rs.addToStrongbox(ResourceFactory.createResource(ResourceType.COIN,5));
        assertEquals(10,  rs.getStrongbox().howManyDoIHave(ResourceType.COIN));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void subToWarehouse(int index) {
        switch (index){
            case 0:
                //TooMuchResourceDepotException
                Resource resourceTooBig= ResourceFactory.createResource(ResourceType.SHIELD, 4);
                assertThrows(NegativeResourceException.class, ()->rs.subToWarehouse(true, 1,resourceTooBig));
                break;
            case 1:
                //InvalidOrganizationWarehouseException
                assertThrows(InvalidOrganizationWarehouseException.class, ()->rs.subToWarehouse(true, 1,ResourceFactory.createResource(ResourceType.COIN, 1)));
                break;
            case 2:
                assertDoesNotThrow(() -> rs.subToWarehouse(true,1,ResourceFactory.createResource(ResourceType.SHIELD,1)));

                Resource r1=ResourceFactory.createResource(ResourceType.SERVANT, 2);
                rs.addLeaderDepot(new Depot(r1, true, 10));
                assertDoesNotThrow(() -> rs.subToWarehouse(false,0,ResourceFactory.createResource(ResourceType.SERVANT,1)));
                break;
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void subtractToStrongbox(int index) {
        switch (index){
            case 0:
                Resource resourceTooBig= ResourceFactory.createResource(ResourceType.SHIELD, 4);
                assertThrows(NegativeResourceException.class, ()->rs.subToStrongbox(resourceTooBig));
                break;
            case 1:
                assertDoesNotThrow( () -> rs.subToStrongbox(ResourceFactory.createResource(ResourceType.COIN,3)));
                assertEquals(2,  rs.getStrongbox().howManyDoIHave(ResourceType.COIN));
                break;
        }

    }

    @Test
    void addToBuffer() {
        //No idea how to test it
    }

    @Test
    void subtractToBuffer() {
        //No idea how to test it
    }

    @Test
    void addToResourcesToProduce() {
        //No idea how to test it
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void canIAfford(int index) {
        rs.newTurn();
        switch (index){
            case 0:
                rs.addDiscount(ResourceFactory.createResource(ResourceType.COIN,1));
                ArrayList<Resource> resourcesCosts1 = new ArrayList<>();
                resourcesCosts1.add(ResourceFactory.createResource(ResourceType.COIN,7));
                resourcesCosts1.add(ResourceFactory.createResource(ResourceType.SERVANT,3));
                resourcesCosts1.add(ResourceFactory.createResource(ResourceType.STONE,1));

                assertTrue(rs.canIAfford(resourcesCosts1,true));
                break;
            case 1:
                ArrayList<Resource> resourcesCosts2 = new ArrayList<>();
                resourcesCosts2.add(ResourceFactory.createResource(ResourceType.STONE,1));
                resourcesCosts2.add(ResourceFactory.createResource(ResourceType.ANY,2));

                assertTrue(rs.canIAfford(resourcesCosts2,false));
                break;
            case 2:
                ArrayList<Resource> resourcesCosts3 = new ArrayList<>();
                resourcesCosts3.add(ResourceFactory.createResource(ResourceType.STONE,1));
                resourcesCosts3.add(ResourceFactory.createResource(ResourceType.COIN,10));
                assertFalse(rs.canIAfford(resourcesCosts3, false));
                break;
        }
    }

    @Test
    void switchResourceFromDepotToDepot() {
        assertDoesNotThrow(()->rs.switchResourceFromDepotToDepot(0,true, 1, true));
        rs.print();
    }

    @Test
    void switchLeaderDepot() {
        rs.addLeaderDepot(new Depot(
                ResourceFactory.createResource(ResourceType.SHIELD, 2),
                true,
                4
        ));

        assertDoesNotThrow(()->rs.switchResourceFromDepotToDepot(0,false,1, true));
        rs.print();
    }

    @Test
    void addLeaderDepot() {
        //Already tested the method it calls
    }

    @Test
    void addDiscount() {
        //No idea how to test it, the discount array is private
    }

    @Test
    void clearBuffers() {
        //No idea how to test it, the buffers are not public
    }

    @Test
    void attachObserver() {
    }

    @Test
    void notifyAllObservers() {
    }
}