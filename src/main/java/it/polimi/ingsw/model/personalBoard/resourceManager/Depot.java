package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.exception.InvalidOrganizationWarehouseException;
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
    public Depot(Resource resource, int maxStorable) {
        this.resource = resource;
        this.maxStorable = maxStorable;
        this.lockDepot = true;
    }

    /**
    *Constructor of Depot with only lockDepot and maxStorable
    */
    public Depot(int maxStorable) {
        this.resource = ResourceFactory.createResource(ResourceType.ANY, 0);
        this.maxStorable = maxStorable;
        this.lockDepot = false;
    }

    /**
     * set the resource, called the first time i need to put in this depot a new resource
     * */
    public void setResource(Resource resource) throws TooMuchResourceDepotException, InvalidOrganizationWarehouseException {
        if (lockDepot && resource.getType() != this.resource.getType()){
            throw new InvalidOrganizationWarehouseException("You can't insert a " + resource.getType() + " in a " +
                    "leader depot of " + this.resource.getType());
        }

        if(resource.getValue()>maxStorable){
            throw new TooMuchResourceDepotException("You tried to put more resources than possible");
        }
        this.resource = resource;
    }

    public void setEmptyResource(){
        if (lockDepot) {
            resource = ResourceFactory.createResource(resource.getType(), 0);
        }else{
            resource = ResourceFactory.createResource(ResourceType.ANY, 0);
        }

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
    public void addResource(Resource newRes) throws TooMuchResourceDepotException{
        if (newRes.getValue() + resource.getValue() > maxStorable){
            throw new TooMuchResourceDepotException("Adding too much res in this depot");
        }
        if (resource.getType() == ResourceType.ANY){
            resource = newRes;
        }else{
            resource.addValue(newRes.getValue());
        }

    }

    /**
     *Sub a value to the value of my resource
     */
    public void subResource(Resource newRes) throws NegativeResourceException, InvalidOrganizationWarehouseException {
        if(resource.getType() == ResourceType.ANY || (lockDepot && resource.getValue() == 0)){
            throw new NegativeResourceException("No resource here!");
        }
        if(resource.getType() != newRes.getType()){
            throw new InvalidOrganizationWarehouseException("You try to sub a resource type different from his own");
        }
        int delta = resource.getValue() - newRes.getValue();
        if(delta < 0){
            throw new NegativeResourceException("You can't sub more resources than are present");
        }else{
            if (delta == 0){
                setEmptyResource();
            }else{
                resource.subValue(newRes.getValue());
            }

        }
    }

}
