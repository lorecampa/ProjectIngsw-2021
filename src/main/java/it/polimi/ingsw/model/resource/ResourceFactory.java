package it.polimi.ingsw.model.resource;

import java.util.ArrayList;

public class ResourceFactory {

    public static Resource createResource(String type, int value){
        return new Resource(type, value);
    }

    public static ArrayList<Resource> createAllResource(){
        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(new Resource("Coin", 0));
        resources.add(new Resource("Shield", 0));
        resources.add(new Resource("Servant", 0));
        resources.add(new Resource("Stone", 0));
        return resources;
    }

}
