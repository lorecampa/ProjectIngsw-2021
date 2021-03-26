package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.exception.TooMuchResourceDepotException;
import it.polimi.ingsw.model.resource.Resource;

public class Depot {
    private Resource resource;
    private boolean lockDepot;
    private int maxStorable;
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

}
