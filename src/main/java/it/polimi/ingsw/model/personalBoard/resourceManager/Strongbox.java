package it.polimi.ingsw.model.personalBoard.resourceManager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;

/**
 * Strongbox is the class where we store the data and manage the resource of the Strongbox*/
public class Strongbox{


    private ArrayList<Resource> resources;


    /**
     * Constructor of Strongbox, set up all the resources
     * */
    @JsonCreator
    public Strongbox() {
        //resources=ResourceFactory.createAllConcreteResource();
        setUpForDebug();
    }

    /**
     * Set 20 as value of every resource in strongbox
     * */
    private void setUpForDebug(){
        resources = new ArrayList<>();
        resources.add(ResourceFactory.createResource(ResourceType.COIN, 20));
        resources.add(ResourceFactory.createResource(ResourceType.SHIELD, 20));
        resources.add(ResourceFactory.createResource(ResourceType.SERVANT, 20));
        resources.add(ResourceFactory.createResource(ResourceType.STONE, 20));
    }

    /**
     * Change the resource of the type passed adding the value passed
     * @param resource to add to the existing one
     * */
    public void addResource(Resource resource){
        resources.get(resources.indexOf(resource)).addValue(resource.getValue());
    }

    /**
     * Change the resource of type passed subtracting the value passed
     * @param resource to sub to the existing one
     * @throws NegativeResourceException if trying to sub more than Strongbox own
     * */
    public void subResource(Resource resource) throws NegativeResourceException {
        resources.get(resources.indexOf(resource)).subValue(resource.getValue());
    }

    /**
     * Return the value that i own of that specific ResourceType
     * @param resourceType i want to find*/
    public int howManyDoIHave(ResourceType resourceType){
        return resources.get(resources.indexOf(ResourceFactory.createResource(resourceType, 0))).getValue();
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    /**
     * Return the array of resourceData read to be sent
     * @return the arrayList of ResourceData to sent
     * */
    public ArrayList<ResourceData> toStrongboxData(){
        ArrayList<ResourceData> strongboxData = new ArrayList<>();
        for (Resource resource: resources)
            strongboxData.add(resource.toClient());
        return strongboxData;
    }

}
