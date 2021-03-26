package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.exception.CantModifyDepotException;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.exception.TooMuchResourceDepotException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * Depot is the class where we store the data and manage the resource of a single depot*/
public class Depot {
    private Resource resource;
    private final boolean lockDepot;
    private final int maxStorable;

    /**
    *Main constructor of Depot with all the attributes
    */
    public Depot(Resource resource, int maxStorable, boolean lockDepot) {
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
    }

    /**
     * set the resource, called the first time i need to put in this depot a new resource
     * */
    public void setResource(Resource resource) throws TooMuchResourceDepotException, CantModifyDepotException{
        if(lockDepot){
            throw new CantModifyDepotException("You can't modify the resource in this depot, it is locked!!!");
        }
        if(resource.getValue()>maxStorable){
            throw new TooMuchResourceDepotException("You tried to put more res then possible");
        }
        this.resource = resource;
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
    public int howMuchResCanIStoreIn(){
        return maxStorable-resource.getValue();
    }

    /**
     *add a value (with sign) to the value of my resource
     */
    public void addValueResource(int value) throws TooMuchResourceDepotException{
        if(value+resource.getValue()>maxStorable){
            throw new TooMuchResourceDepotException("Adding too much res in this depot");
        }
        try{
            resource.addValue(value);
        }
        catch (NegativeResourceException e){
            e.printStackTrace();
        }
    }

}
