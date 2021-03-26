package it.polimi.ingsw.model.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResourceFactoryTest {

    List<Resource> resourceArray = new ArrayList<>();
    Resource resource;

    @BeforeEach
    void init(){
        resourceArray.add(new Resource(ResourceType.COIN, 0));
        resourceArray.add(new Resource(ResourceType.SHIELD, 0));
        resourceArray.add(new Resource(ResourceType.SERVANT, 0));
        resourceArray.add(new Resource(ResourceType.STONE, 0));

        resource = new Resource(ResourceType.STONE, 0);
    }

    @Test
    void createResource() {
        Resource r1 = ResourceFactory.createResource(ResourceType.STONE, 0);
        assertEquals(resource.getType(), r1.getType());
        assertEquals(resource.getValue(), r1.getValue());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void createAllConcreteResource(int index) {
        List<Resource> r1 = ResourceFactory.createAllConcreteResource();
        assertEquals(r1.size(), resourceArray.size());
        assertEquals(r1.get(index).getType(), resourceArray.get(index).getType());
        assertEquals(r1.get(index).getValue(), resourceArray.get(index).getValue());
    }

}