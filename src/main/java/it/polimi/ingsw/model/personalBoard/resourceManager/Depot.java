package it.polimi.ingsw.model.personalBoard.resourceManager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    *Main constructor of Depot with all the attributes, used for leader's depot
     * @param maxStorable integer representing the max value of local resource
     * @param resource  to initialize at
    */
    @JsonCreator
    public Depot(@JsonProperty("resource") Resource resource,
                 @JsonProperty("maxStorable") int maxStorable) {
        this.resource = resource;
        this.maxStorable = maxStorable;
        this.lockDepot = true;
    }

    /**
    *Constructor of Depot with only lockDepot and maxStorable, used for normal depots
     *  @param maxStorable integer representing the max value of local resource
    */
    public Depot(int maxStorable) {
        this.resource = ResourceFactory.createResource(ResourceType.ANY, 0);
        this.maxStorable = maxStorable;
        this.lockDepot = false;
    }

    /**
     * Set resource of curr Depot
     * @param resource to set the curr depot at
     * @throws TooMuchResourceDepotException if You tried to put more resources than possible
     * @throws InvalidOrganizationWarehouseException if You can't insert a RESOURCETYPE_A in a leader depot of RESOURCETYPE_B
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

    /**
     * Set the curr resource at value 0 and reset the type if is not a lockDepot
     * */
    public void setEmptyResource(){
        if (lockDepot) {
            resource = ResourceFactory.createResource(resource.getType(), 0);
        }else{
            resource = ResourceFactory.createResource(ResourceType.ANY, 0);
        }

    }

    /**
     *Return the value of resource in depot
     * @return the value of resource in depot
     */
    public int getResourceValue(){
        return resource.getValue();
    }

    /**
     *Return the type of resource in depot
     * @return the type of resource in depot
     */
    public ResourceType getResourceType(){
        return resource.getType();
    }

    /**
     *Return how many free spaces available in curr depot
     * @return how many free spaces available in curr depot
     */
    public int howMuchResCanIStillStoreIn(){
        return Math.max(maxStorable - resource.getValue(), 0);
    }

    /**       
     *Add a resource to the curr depot, adding the value if possible
     * @param newRes to add to curr depot
     * @throws TooMuchResourceDepotException if adding too much res in this depot
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
     *Sub a resource to the curr depot, subtracting the value if possible
     * @param newRes to sub to curr depot
     * @throws NegativeResourceException if there are no resources here or if you can't sub more resources than are present
     * @throws  InvalidOrganizationWarehouseException if you try to sub a resource type different from his own
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
