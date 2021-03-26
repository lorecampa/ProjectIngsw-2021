package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.exception.CantModifyDepotException;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.exception.TooMuchResourceDepotException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepotTest {
    Resource r1, r2;
    Depot d1, d2;
    @BeforeEach
    void init(){
        r1 = ResourceFactory.createResource(ResourceType.COIN, 2);
        r2 = ResourceFactory.createResource(ResourceType.COIN, 4);
        d1 = new Depot(true, 2);    //depot with lockedDepot:true
        d2 = new Depot(false, 2);   //depot with lockedDepot:false
    }

    @Test
    void setResource() {
        assertThrows(CantModifyDepotException.class,()->{d1.setResource(r1);});
        assertThrows(TooMuchResourceDepotException.class , ()->{d2.setResource(r2);});
    }

    @Test
    void getResourceValue() {

    }

    @Test
    void getResourceType(){

    }

    @Test
    void howMuchResCanIStoreIn() {
    }

    @Test
    void addValueResource() {
    }
}