package it.polimi.ingsw.model.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.support.hierarchical.ThrowableCollector;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTest {
    Resource resourceCoin, resourceStone;

    @BeforeEach
    void init(){
        resourceCoin= new Resource(ResourceType.COIN, 5);
        resourceStone= new Resource(ResourceType.STONE, 10);
    }

    @Test
    void getValue() {
        assertEquals(5, resourceCoin.getValue());
    }

    @Test
    void getType() {
        assertEquals(ResourceType.COIN, resourceCoin.getType());
        assertEquals(ResourceType.STONE, resourceStone.getType());
    }
}