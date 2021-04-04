package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrongboxTest {
    Strongbox s;

    @BeforeEach
    void init(){
        s=new Strongbox();
        Resource r = ResourceFactory.createResource(ResourceType.SHIELD, 4);
        s.changeResourceValueOf(r);
        //s.print();
    }

    @Test
    void changeResourceOf() {
        Resource r = ResourceFactory.createResource(ResourceType.COIN, 5);
        s.changeResourceValueOf(r);
        r = ResourceFactory.createResource(ResourceType.SHIELD, 4);
        s.changeResourceValueOf(r);
        //s.print();
    }

    @Test
    void howManyDoIHave() {
        assertEquals(4, s.howManyDoIHave(ResourceType.SHIELD));
    }
}