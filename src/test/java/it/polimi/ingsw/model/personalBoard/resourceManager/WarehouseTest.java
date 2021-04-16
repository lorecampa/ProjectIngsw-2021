package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.exception.CantModifyDepotException;
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
class WarehouseTest {

    Warehouse w = new Warehouse();
    Resource resourceTooBig = ResourceFactory.createResource(ResourceType.COIN, 100);
    Resource resourceNegative = ResourceFactory.createResource(ResourceType.COIN, -50);
    @BeforeEach
    void init(){
        assertDoesNotThrow( ()->w.setResourceDepotAt(0, ResourceFactory.createResource(ResourceType.COIN, 1)));
        assertDoesNotThrow( ()->w.setResourceDepotAt(1, ResourceFactory.createResource(ResourceType.SHIELD, 1)));
        assertDoesNotThrow( ()->w.setResourceDepotAt(2, ResourceFactory.createResource(ResourceType.STONE, 1)));
    }

    @Test
    void getDepot(){
        assertEquals(ResourceType.COIN, w.getDepot(0).getResourceType());
        assertEquals(1, w.getDepot(0).getResourceValue());
    }

    @Test
    void getDepotLeader(){
        Depot leaderDepot=new Depot(true, 4);
        w.addDepotLeader(leaderDepot);
        assertEquals(leaderDepot, w.getDepotLeader(0));
    }

    @Test
    void addDepotLeader() {
        w.addDepotLeader(new Depot(true, 4));
        assertEquals(1, w.copyDepotsLeader().size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void addToLeaderDepotValueAt(int index){
        w.addDepotLeader(new Depot(ResourceFactory.createResource(ResourceType.COIN, 2),true, 4));
        switch(index){
            case 0:
                //TooMuchResourceDepotException
                assertThrows(TooMuchResourceDepotException.class, ()->w.addToLeaderDepotValueAt(0,resourceTooBig));
                break;
            case 1:
                assertDoesNotThrow(()->w.addToLeaderDepotValueAt(0,ResourceFactory.createResource(ResourceType.COIN, 2)));
                break;
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void subToLeaderDepotValueAt(int index){
        w.addDepotLeader(new Depot(ResourceFactory.createResource(ResourceType.COIN, 2),true, 4));
        switch(index){
            case 0:
                //NegativeResourceException
                assertThrows(NegativeResourceException.class, ()->w.subToLeaderDepotValueAt(0,resourceNegative));
                break;
            case 1:
                assertDoesNotThrow(()->w.subToLeaderDepotValueAt(0,ResourceFactory.createResource(ResourceType.COIN, 1)));
                break;
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void addToStandardDepotValueAt(int index){
        switch(index){
            case 0:
                //TooMuchResourceDepotException
                assertThrows(TooMuchResourceDepotException.class, ()->w.addToStandardDepotValueAt(0,resourceTooBig));
                break;
            case 1:
                //InvalidOrganizationWarehouseException
                assertThrows(InvalidOrganizationWarehouseException.class, ()->w.addToStandardDepotValueAt(2,ResourceFactory.createResource(ResourceType.COIN, 1)));
                break;
            case 2:
                assertDoesNotThrow(()->w.addToStandardDepotValueAt(1,ResourceFactory.createResource(ResourceType.SHIELD, 1)));
                break;
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void subToStandardDepotValueAt(int index){
        switch(index){
            case 0:
                //NegativeResourceException
                assertThrows(NegativeResourceException.class, ()->w.subToStandardDepotValueAt(0,resourceNegative));
                break;
            case 1:
                assertDoesNotThrow(()->w.subToStandardDepotValueAt(1,ResourceFactory.createResource(ResourceType.SHIELD, 1)));
                w.print();
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

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void setResourceDepotAt(int index){
        switch (index){
            case 0:
                //InvalidOrganizationWarehouseException test
                assertThrows(InvalidOrganizationWarehouseException.class , ()-> w.setResourceDepotAt(1, ResourceFactory.createResource(ResourceType.COIN, 1)));
                break;
            case 1:
                //TooMuchResourceDepotException test
                assertThrows(TooMuchResourceDepotException.class , ()-> w.setResourceDepotAt(2, ResourceFactory.createResource(ResourceType.SERVANT, 5)));
                break;
            case 2:
                Warehouse w1 = new Warehouse();
                assertDoesNotThrow( ()->w1.setResourceDepotAt(0, ResourceFactory.createResource(ResourceType.COIN, 1)));
                assertDoesNotThrow( ()->w1.setResourceDepotAt(1, ResourceFactory.createResource(ResourceType.SHIELD, 1)));
                assertDoesNotThrow( ()->w1.setResourceDepotAt(2, ResourceFactory.createResource(ResourceType.STONE, 1)));
                assertDoesNotThrow( ()->w1.setResourceDepotAt(0, ResourceFactory.createResource(ResourceType.SERVANT, 1)));
                break;
            default:
        }
    }

    @Test
    void removeResourceAt(){
        Resource r = w.removeResourceAt(0);
        assertEquals(ResourceFactory.createResource(ResourceType.ANY, 1),  w.getDepot(0).getResource());
        assertEquals(ResourceFactory.createResource(ResourceType.COIN, 1), r);
    }

    @Test
    void howManyDoIHave(){
        assertEquals(1, w.howManyDoIHave(ResourceType.SHIELD));
    }

    @Test
    void copyDepots(){
        ArrayList<Depot> depoCopy;
        depoCopy=w.copyDepots();
        assertEquals(3, depoCopy.size());
    }

    @Test
    void copyDepotsLeader(){
        w.addDepotLeader(new Depot(true, 4));
        ArrayList<Depot> depoLeaderCopy;
        depoLeaderCopy=w.copyDepotsLeader();
        assertEquals(1, depoLeaderCopy.size());
    }

}