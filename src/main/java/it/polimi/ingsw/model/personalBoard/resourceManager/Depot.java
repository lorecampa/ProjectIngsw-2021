package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.exception.CantModifyDepotException;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.exception.TooMuchResourceDepotException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * Depot is the class where we store the data and manage the resource of a single depot*/
public class Depot {
    private Resource resource;
    private final boolean lockDepot;
    private final int maxStorable;

    public Resource getResource() {
        return resource;
    }

    public boolean isLockDepot() {
        return lockDepot;
    }

    public int getMaxStorable() {
        return maxStorable;
    }

    /**
    *Main constructor of Depot with all the attributes
    */
    public Depot(Resource resource, boolean lockDepot, int maxStorable) {
        this.resource = resource;
        this.maxStorable = maxStorable;
        this.lockDepot = lockDepot;
    }

    /**
    *Constructor of Depot with only lockDepot and maxStorable
    */
    public Depot(boolean lockDepot, int maxStorable) {
        this.lockDepot = lockDepot;
        this.maxStorable = maxStorable;
        if(!lockDepot){
            this.resource = ResourceFactory.createResource(ResourceType.ANY, 0);
        }
    }

    /**
     * set the resource, called the first time i need to put in this depot a new resource
     * */
    public void setResource(Resource resource) throws TooMuchResourceDepotException{
        if(resource.getValue()>maxStorable){
            throw new TooMuchResourceDepotException("You tried to put more res then possible");
        }
        this.resource = resource;
    }

    public void setEmptyResource(){
        this.resource = ResourceFactory.createResource(ResourceType.ANY, 0);
    }

    /**
     *return the value of resource in depot
     */
    public int getResourceValue(){
        return resource.getValue();
    }

    /**
     *return the type of resource in depot
     */
    public ResourceType getResourceType(){
        return resource.getType();
    }

    /**
     *return the "free" spaces available in this depot
     */
    public int howMuchResCanIStillStoreIn(){
        return Math.max(maxStorable - resource.getValue(), 0);
    }

    /**       
     *Add a value to the value of my resource
     */
    public void addValueResource(int value) throws TooMuchResourceDepotException{
        if(value+resource.getValue()>maxStorable){
            throw new TooMuchResourceDepotException("Adding too much res in this depot");
        }
        resource.addValue(value);
    }

    /**
     *Sub a value to the value of my resource
     */
    public void subValueResource(int value) throws NegativeResourceException{
        resource.subValue(value);
        if(!lockDepot && value==0)
            setEmptyResource();
    }

    @Override
    public String toString() {
        return resource.getType()+" ("+resource.getValue()+"/"+maxStorable+")";
    }
}
