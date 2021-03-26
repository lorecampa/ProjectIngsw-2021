package it.polimi.ingsw.model.resource;

import java.util.ArrayList;

public class ResourceFactory {

    public static Resource createResource(ResourceType type, int value){
        return new Resource(type, value);
    }

    public static ArrayList<Resource> createAllConcreteResource(){
        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(new Resource(ResourceType.COIN, 0));
        resources.add(new Resource(ResourceType.SHIELD, 0));
        resources.add(new Resource(ResourceType.SERVANT, 0));
        resources.add(new Resource(ResourceType.STONE, 0));
        return resources;
    }

}
