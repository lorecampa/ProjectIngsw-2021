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
     * @param resource: the resource to add to the existing one*/
    public void changeResourceValueOf(Resource resource){
        try{
            resources.get(resources.indexOf(resource)).addValue(resource.getValue());
        }
        catch(NegativeResourceException e){
            e.printStackTrace();
        }
    }

    /**
     * return the value that i own of that specific ResourceType
     * @param resourceType: the type i want to analyze*/
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
