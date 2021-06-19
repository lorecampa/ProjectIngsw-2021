package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.NegativeResourceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTest {
    Resource resourceCoin, resourceStone;

    @BeforeEach
    void init(){
        resourceCoin= new Resource(ResourceType.COIN, 5);
        resourceStone= new Resource(ResourceType.STONE, 10);
    }

    @Test
    void getValueTest() {
        assertEquals(5, resourceCoin.getValue());
    }


    @Test
    void getTypeTest() {
        assertEquals(ResourceType.COIN, resourceCoin.getType());
        assertEquals(ResourceType.STONE, resourceStone.getType());
    }

    @Test
    void getDisplayNameTest(){
        assertEquals("Coin", resourceCoin.getType().getDisplayName());
    }

    @Test
    void setValueTest(){
        resourceCoin.setValueToZero();
        assertEquals(0, resourceCoin.getValue());
        resourceCoin.addValue(1);
        assertEquals(1, resourceCoin.getValue());
        assertThrows(NegativeResourceException.class, ()-> resourceCoin.subValue(2));
        assertDoesNotThrow(()->resourceCoin.subValue(1));
        assertEquals(0, resourceCoin.getValue());
    }

    @Test
    void toClientTest(){
        ResourceData resourceData = resourceCoin.toClient();
        assertEquals(5,resourceData.getValue());
        assertEquals(ResourceType.COIN,resourceData.getType());

        assertEquals("COIN: 5", resourceCoin.toString());
    }

    @Test
    void equalsTest(){
        assertNotEquals(resourceCoin, null);
        assertNotEquals(resourceCoin, ResourceFactory.createResource(ResourceType.STONE, 2));
        assertNotEquals(resourceCoin, new Object());
        assertEquals(resourceCoin,resourceCoin);
    }
}