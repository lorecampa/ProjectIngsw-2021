package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.model.resource.Resource;

public class Depot {
    private Resource resource;
    private boolean lockDepot;
    private int maxStorable;

    public Depot(Resource resource,int maxStorable, boolean lockDepot) {
        this.resource = resource;
        this.maxStorable = maxStorable;
        this.lockDepot = lockDepot;
    }

    public Depot(Resource resource, int maxStorable) {
        this.resource = resource;
        this.maxStorable = maxStorable;
    }

}
