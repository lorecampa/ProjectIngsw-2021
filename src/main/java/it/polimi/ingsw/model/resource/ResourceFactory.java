package it.polimi.ingsw.model.resource;

import java.util.ArrayList;

/**
 * Resource Factory define a class used to create a Resource.
 */
public class ResourceFactory {

    /**
     * Return a resource based on type e value.
     * @param type the resource type.
     * @param value te resource value.
     * @return a resource based on type e value.
     */
    public static Resource createResource(ResourceType type, int value){
        return new Resource(type, value);
    }

    /**
     * Return an ArrayList with all the concreteResources, COIN, SHIELD, SERVANT and STONE.
     * @return an ArrayList with all the concreteResources, COIN, SHIELD, SERVANT and STONE.
     */
    public static ArrayList<Resource> createAllConcreteResource(){
        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(new Resource(ResourceType.COIN, 0));
        resources.add(new Resource(ResourceType.SHIELD, 0));
        resources.add(new Resource(ResourceType.SERVANT, 0));
        resources.add(new Resource(ResourceType.STONE, 0));
        return resources;
    }
}
