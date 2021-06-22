package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StrongboxTest {
    Strongbox s;

    @BeforeEach
    void init(){
        s=new Strongbox();

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void addResourceValueOf(int  index){
        s.addResource(ResourceFactory.createResource(ResourceType.SHIELD,  2));
        assertEquals(2, s.howManyDoIHave(ResourceType.SHIELD));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void subResourceValueOf(int index){
        switch (index){
            case 0:
                assertThrows(NegativeResourceException.class, ()->s.subResource( ResourceFactory.createResource(ResourceType.COIN, 1)));
                break;
            case 1:
                s.addResource(ResourceFactory.createResource(ResourceType.SHIELD,  2));
                assertDoesNotThrow(()->s.subResource(ResourceFactory.createResource(ResourceType.SHIELD,  1)));
                assertEquals(1, s.howManyDoIHave(ResourceType.SHIELD));
                break;
        }
    }

    @Test
    void howManyDoIHave() {
        s.addResource(ResourceFactory.createResource(ResourceType.SHIELD,  2));
        assertEquals(2, s.howManyDoIHave(ResourceType.SHIELD));
    }

    @Test
    void getRes(){
        ArrayList<Resource> res = s.getResources();
        assertTrue(res.contains(ResourceFactory.createResource(ResourceType.STONE,1)));
        assertTrue(res.contains(ResourceFactory.createResource(ResourceType.COIN,1)));
        assertTrue(res.contains(ResourceFactory.createResource(ResourceType.SERVANT,1)));
        assertTrue(res.contains(ResourceFactory.createResource(ResourceType.SHIELD,1)));

    }
}