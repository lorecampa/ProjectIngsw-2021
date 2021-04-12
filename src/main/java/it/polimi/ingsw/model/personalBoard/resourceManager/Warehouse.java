package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.exception.CantModifyDepotException;
import it.polimi.ingsw.exception.InvalidOrganizationWarehouseException;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.exception.TooMuchResourceDepotException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Warehouse implements Cloneable{
    private ArrayList<Depot> depots = new ArrayList<>();
    private ArrayList<Depot> depotsLeader = new ArrayList<>();


    public Warehouse() {
        for (int i = 0; i < 3; i++) {
            depots.add(new Depot(false, i+1));
        }
    }

    /**
     * Constructor to clone the warehouse when we enter the edit mode*/
    public Warehouse(ArrayList<Depot> depots, ArrayList<Depot> depotsLeader){
        this.depots=depots;
        this.depotsLeader=depotsLeader;
    }

    /**
     * Get a specific standard depot
     * @param index of the depot i want
     * @return the depot at index pos*/
    public Depot getDepot(int index){
        return depots.get(index);
    }

    /**
     * Get a specific leader depot
     * @param index of the depot i want
     * @return the depot at index pos*/
    public Depot getDepotLeader(int index){
        return depotsLeader.get(index);
    }

    /**
     * Method used to add a depot to the leader list when a WarehouseLeader is active
     * @param depot: the depot i want to add*/
    public void addDepotLeader(Depot depot){
        depotsLeader.add(depot);
    }

    /**
     * call the private modifyDepotValue with the leader depots list
     * @param indexDepot the index i want to modify in the normal depot list
     * @param resource the resource to sum*/
    public void addToLeaderDepotValueAt(int indexDepot, Resource resource) throws TooMuchResourceDepotException{
        if(depotsLeader.get(indexDepot).getResourceType()==resource.getType()){
            depotsLeader.get(indexDepot).addValueResource(resource.getValue());
        }

        //altrimenti non dovresti lanciare una eccezione? se sto provando ad inserire una risorsa in uno
        //scaffale che non posso?
    }

    /**
     * call the private modifyDepotValue with the leader depots list
     * @param indexDepot the index i want to modify in the normal depot list
     * @param resource the resource to sub*/
    public void subToLeaderDepotValueAt(int indexDepot, Resource resource) throws NegativeResourceException, TooMuchResourceDepotException{
        if(depotsLeader.get(indexDepot).getResourceType()==resource.getType()){
            depotsLeader.get(indexDepot).subValueResource(resource.getValue());
        }
    }

    /**
     * call the private modifyDepotValue with the standard depots list
     *@param indexDepot the index i want to modify in the leader depot list
     *@param resource the resource to sum*/
    public void addToStandardDepotValueAt(int indexDepot, Resource resource) throws TooMuchResourceDepotException, InvalidOrganizationWarehouseException, CantModifyDepotException {
        if(depots.get(indexDepot).getResourceType()==resource.getType()){
            depots.get(indexDepot).addValueResource(resource.getValue());
        }
        else{
            setResourceDepotAt(indexDepot, resource);
        }
    }

    /**
     * call the private modifyDepotValue with the standard depots list
     *@param indexDepot the index i want to modify in the leader depot list
     *@param resource the resource to sub*/
    public void subToStandardDepotValueAt(int indexDepot, Resource resource) throws NegativeResourceException, TooMuchResourceDepotException, InvalidOrganizationWarehouseException, CantModifyDepotException {
        if(depots.get(indexDepot).getResourceType()==resource.getType()){
            depots.get(indexDepot).subValueResource(resource.getValue());
        }
        else{
            setResourceDepotAt(indexDepot, resource);
        }
    }

    /**
     * used to know if there's already a depot with that resource type in the warehouse
     * @param type: the type i'm looking at
     * @return true if i already have one, false if i don't*/
    public boolean doIHaveADepotWith(ResourceType type){
        for(Depot dep: depots){
            if(dep.getResourceType()==type){
                return true;
            }
        }
        return false;
    }

    /**
     * used to set a new resource into a chosen depot, used when we have to change resource from one  depot to another
     * @param indexDepot: the index i'm modifying in the depots arraylist
     * @param resource: the new resource to set at that index*/
    public void setResourceDepotAt(int indexDepot, Resource resource) throws InvalidOrganizationWarehouseException, TooMuchResourceDepotException, CantModifyDepotException {
        if(doIHaveADepotWith(resource.getType())){
            throw new InvalidOrganizationWarehouseException(    "You can't put 2 same type of res in 2 different depot! Try a new move!\n" +
                                                                "U may have tried to put the same res type in a depot that contain that type");
        }
        //resourceToManage= ResourceFactory.createResource(depots.get(indexDepot).getResourceType(), depots.get(indexDepot).getResourceValue());
        depots.get(indexDepot).setResource(resource);
    }

    /**
     * Remove the resource from the standard depot
     * @param index of depot i want to empty
     * @return the resource i took from the index depot*/
    public Resource removeResourceAt(int index){
        Resource resource = depots.get(index).getResource();
        depots.get(index).setEmptyResource();
        return resource;
    }

    /**
     * used to know how much resource of a single type i'm storing in the warehouse(Leader + Normal)
     * @param resourceType: the type of the resource i want to find
     * @return the amount value of resourceType i own*/
    public int howManyDoIHave(ResourceType resourceType){
        int num=0;
        for(Depot dep:depots){
            if(dep.getResourceType()==resourceType){
                num+=dep.getResourceValue();
            }
        }
        for(Depot dep : depotsLeader){
            if(dep.getResourceType()==resourceType){
                num+=dep.getResourceValue();
            }
        }
        return num;
    }

    /**
     * Copy the standard array depot
     * @return a clone of the standard depots*/
    public ArrayList<Depot> copyDepots(){
        return depots.stream()
                .map(Dep -> new Depot(Dep.getResource(), Dep.isLockDepot(), Dep.getMaxStorable()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Copy the leader array depot
     * @return a clone of the leader depots*/
    public ArrayList<Depot> copyDepotsLeader(){
        return depotsLeader.stream()
                .map(Dep -> new Depot(Dep.getResource(), Dep.isLockDepot(), Dep.getMaxStorable()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Made for testing purposes!*/
    public void print(){
        System.out.println("===================WAREHOUSE======================");

        System.out.println("Normal depot:");
        for(Depot  dep: depots){
            System.out.println(dep);
        }
        System.out.println("Leader depot:");
        for(Depot  dep: depotsLeader){
            System.out.println(dep);
        }
        //System.out.println("Resource to manage:"+ resourceToManage.getType()+"("+resourceToManage.getValue()+")");
        System.out.println("==================================================");
    }
}
