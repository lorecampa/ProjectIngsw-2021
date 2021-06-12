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

import static org.junit.jupiter.api.Assertions.*;
class WarehouseTest {

    Warehouse w = new Warehouse();
    Resource resourceTooBig = ResourceFactory.createResource(ResourceType.COIN, 100);
    Resource resourceNegative = ResourceFactory.createResource(ResourceType.COIN, -50);
    @BeforeEach
    void init(){

        assertDoesNotThrow( ()->w.addDepotResourceAt(0, ResourceFactory.createResource(ResourceType.COIN, 1), true));
        assertDoesNotThrow( ()->w.addDepotResourceAt(1, ResourceFactory.createResource(ResourceType.SHIELD, 1), true));
        assertDoesNotThrow( ()->w.addDepotResourceAt(2, ResourceFactory.createResource(ResourceType.STONE, 1), true));

    }

    @Test
    void getDepot(){
        assertEquals(ResourceType.COIN, w.getDepot(0, true).getResourceType());
        assertEquals(1, w.getDepot(0, true).getResourceValue());
    }



    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void addToLeaderDepotValueAt(int index){
        w.addDepotLeader(new Depot(ResourceFactory.createResource(ResourceType.COIN, 2), 4));
        switch(index){
            case 0:
                //TooMuchResourceDepotException
                assertThrows(TooMuchResourceDepotException.class, ()->w.addDepotResourceAt(0,resourceTooBig, false));
                break;
            case 1:
                assertDoesNotThrow(()->w.addDepotResourceAt(0,ResourceFactory.createResource(ResourceType.COIN, 2), false));
                break;
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void subToLeaderDepotValueAt(int index){
        w.addDepotLeader(new Depot(ResourceFactory.createResource(ResourceType.COIN, 2), 4));
        switch(index){
            case 0:
                //NegativeResourceException
                assertThrows(NegativeResourceException.class, ()->w.subDepotResourceAt(0,resourceNegative, false));
                break;
            case 1:
                assertDoesNotThrow(()->w.subDepotResourceAt(0,ResourceFactory.createResource(ResourceType.COIN, 1), false));
                break;
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void addToStandardDepotValueAt(int index){
        switch(index){
            case 0:
                //TooMuchResourceDepotException
                assertThrows(TooMuchResourceDepotException.class, ()->w.addDepotResourceAt(0,resourceTooBig, true));
                break;
            case 1:
                //InvalidOrganizationWarehouseException
                assertThrows(InvalidOrganizationWarehouseException.class, ()->w.addDepotResourceAt(2,ResourceFactory.createResource(ResourceType.COIN, 1), true));
                break;
            case 2:
                assertDoesNotThrow(()->w.addDepotResourceAt(1,ResourceFactory.createResource(ResourceType.SHIELD, 1), true));
                break;
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void subToStandardDepotValueAt(int index){
        switch(index){
            case 0:
                //NegativeResourceException
                assertThrows(NegativeResourceException.class, ()->w.subDepotResourceAt(0,resourceNegative, true));
                break;
            case 1:
                assertDoesNotThrow(()->w.subDepotResourceAt(1,ResourceFactory.createResource(ResourceType.SHIELD, 1), true));
                break;
        }

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void doIHaveADepotWith(int index) {
        switch(index){
            case 0:
                assertTrue(w.doIHaveADepotWith(ResourceType.STONE));
                break;
            case 1:
                assertFalse(w.doIHaveADepotWith(ResourceType.SERVANT));
                break;
        }
    }



    @Test
    void removeResourceAt(){
        Resource r = w.popResourceFromDepotAt(0, true);
        assertEquals(ResourceFactory.createResource(ResourceType.ANY, 1),  w.getDepot(0, true).getResource());
        assertEquals(ResourceFactory.createResource(ResourceType.COIN, 1), r);
    }

    @Test
    void howManyDoIHave(){
        assertEquals(1, w.howManyDoIHave(ResourceType.SHIELD));
    }



}