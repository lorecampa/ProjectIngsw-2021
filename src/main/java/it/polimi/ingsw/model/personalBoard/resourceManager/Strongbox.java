package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;


public class Strongbox {
    private ArrayList<Resource> resources;

    public Strongbox() {
        resources=ResourceFactory.createAllConcreteResource();
    }

    /**
     * change the resource passed in resources adding the value passed
     * @param resource to add to the existing one*/
    public void addResourceValueOf(Resource resource){
        resources.get(resources.indexOf(resource)).addValue(resource.getValue());
    }

    /**
     * change the resource passed in resources subtracting the value passed
     * @param resource to sub to the existing one*/
    public void subResourceValueOf(Resource resource) throws NegativeResourceException {
        resources.get(resources.indexOf(resource)).subValue(resource.getValue());
    }

    /**
     * return the value that i own of that specific ResourceType
     * @param resourceType i want to find*/
    public int howManyDoIHave(ResourceType resourceType){
        return resources.get(resources.indexOf(ResourceFactory.createResource(resourceType, 0))).getValue();
    }


    public void print(){
        System.out.println("===================STRONGBOX======================");
        for(Resource res:resources){
            System.out.println(res.getType()+" ("+res.getValue()+")");
        }
        System.out.println("==================================================");
    }
}
