package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;


public class Strongbox {
    private ArrayList<Resource> resources;

    public Strongbox() {
        //resources=ResourceFactory.createAllConcreteResource();
        setUpForDebug();
    }

    private void setUpForDebug(){
        resources = new ArrayList<>();
        resources.add(ResourceFactory.createResource(ResourceType.COIN, 20));
        resources.add(ResourceFactory.createResource(ResourceType.SHIELD, 20));
        resources.add(ResourceFactory.createResource(ResourceType.SERVANT, 20));
        resources.add(ResourceFactory.createResource(ResourceType.STONE, 20));
    }



    /**
     * change the resource passed in resources adding the value passed
     * @param resource to add to the existing one*/
    public void addResource(Resource resource){
        resources.get(resources.indexOf(resource)).addValue(resource.getValue());
    }

    /**
     * change the resource passed in resources subtracting the value passed
     * @param resource to sub to the existing one*/
    public void subResource(Resource resource) throws NegativeResourceException {
        resources.get(resources.indexOf(resource)).subValue(resource.getValue());
    }

    /**
     * return the value that i own of that specific ResourceType
     * @param resourceType i want to find*/
    public int howManyDoIHave(ResourceType resourceType){
        return resources.get(resources.indexOf(ResourceFactory.createResource(resourceType, 0))).getValue();
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public ArrayList<ResourceData> toStrongboxData(){
        ArrayList<ResourceData> strongboxData = new ArrayList<>();
        for (Resource resource: resources)
            strongboxData.add(resource.toClient());
        return strongboxData;
    }

}
