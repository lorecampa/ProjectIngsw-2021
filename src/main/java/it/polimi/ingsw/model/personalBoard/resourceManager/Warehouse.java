package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.InvalidOrganizationWarehouseException;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.exception.TooMuchResourceDepotException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;
import java.util.ArrayList;
import java.util.OptionalInt;
/**
 * Warehouse class represent the depot storage in the personal board
 * */
public class Warehouse{
    private ArrayList<Depot> depots = new ArrayList<>();
    private ArrayList<Depot> depotsLeader = new ArrayList<>();

    /**
     * Main constructor of warehouse, create all his depots
     * */
    public Warehouse() {
        for (int i = 0; i < 3; i++) {
            depots.add(new Depot(i+1));
        }
    }

    /**
     * Constructor to clone the warehouse when we enter the edit mode
     * */
    public Warehouse(ArrayList<Depot> depots, ArrayList<Depot> depotsLeader){
        this.depots=depots;
        this.depotsLeader=depotsLeader;
    }


    /**
     * Used to add a depot to the leader list when a WarehouseLeader is activated
     * @param depot i want to add
     */
    public void addDepotLeader(Depot depot){
        depotsLeader.add(depot);
    }

    /**
     * Remove the depot from the list of depot Leader
     * @param depotToRemove i want to remove
     * @deprecated
     * */
    public void removeDepotLeader(Depot depotToRemove){
        OptionalInt index = depotsLeader.stream()
                .filter(x -> x.getResourceType() == depotToRemove.getResourceType() &&
                        x.getMaxStorable() == depotToRemove.getMaxStorable())
                .mapToInt(depotsLeader::indexOf).findFirst();

        index.ifPresent(depotsLeader::remove);
    }

    /**
     * Add a resource to a specific depot
     * @param indexDepot the index i want to modify
     * @param resource the resource to sum
     * @param isNormalDepot true if normal depot false if leader depot
     * @throws TooMuchResourceDepotException if adding too much res in this depot
     * @throws InvalidOrganizationWarehouseException if You try to add a resource type different from his own or if you can't add Type_A because you have those already stored in another depot
     * */
    public void addDepotResourceAt(int indexDepot, Resource resource, boolean isNormalDepot) throws TooMuchResourceDepotException, InvalidOrganizationWarehouseException {
        Depot depot = getDepot(indexDepot, isNormalDepot);

        if(depot.getResourceType()!=resource.getType() && depot.getResourceType() != ResourceType.ANY){
            throw new InvalidOrganizationWarehouseException("You try to add a resource type different from his own");
        }
        if(isNormalDepot && (depot.getResourceType() == ResourceType.ANY) && doIHaveADepotWith(resource.getType())){
            throw new InvalidOrganizationWarehouseException("You can't add " + resource.getType() +" because you " +
                    "have those already stored in another depot");
        }
        depot.addResource(resource);
    }




    /**
     * Sub a resource to a specific depot
     *@param indexDepot the index i want to modify in the leader depot list
     *@param resource the resource to sub
     * @param isNormalDepot true if normal depot false if leader depot
     * @throws NegativeResourceException if try to sub more than i own
     * @throws InvalidOrganizationWarehouseException if You try to add a resource type different from his own or if you can't add Type_A because you have those already stored in another depot
     * */
    public void subDepotResourceAt(int indexDepot, Resource resource, boolean isNormalDepot) throws NegativeResourceException, IndexOutOfBoundsException, InvalidOrganizationWarehouseException {
        getDepot(indexDepot, isNormalDepot).subResource(resource);
    }

    /**
     * Return true if i have a depot with a specific type of resource
     * @param type: the type i'm looking at
     * @return true if i already have one, false if i don't*/
    public boolean doIHaveADepotWith(ResourceType type){
        if(type==ResourceType.ANY)
            return false;

        for(Depot dep: depots){
            if(dep.getResourceType()==type){
                return true;
            }
        }
        return false;
    }


    /**
     * Return and remove the resource from depot
     * @param indexDepot of depot i want to empty
     * @param isNormalDepot true if normal, false if leader
     * @return the resource i took from the index depot*/
    public Resource popResourceFromDepotAt(int indexDepot, boolean isNormalDepot) throws IndexOutOfBoundsException{
        Depot depot;
        if(isNormalDepot) depot = depots.get(indexDepot);
        else depot = depotsLeader.get(indexDepot);

        Resource resource = depot.getResource();
        depot.setEmptyResource();
        return resource;
    }

    /**
     * Return how many resource of a single type i'm storing in the warehouse(Leader + Normal)
     * @param resourceType i want to find
     * @return the value of resourceType i own*/
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
     * Restore the depot to original format
     * @param depotIndex to restore
     * @param isNormalDepot true if normal depot, false if leader
     * */
    public void restoreDepot(int depotIndex, boolean isNormalDepot){
        Depot depot = getDepot(depotIndex, isNormalDepot);
        depot.setEmptyResource();
    }

    /**
     * Return a normal depot
     * @param index of the depot i want
     * @return the normal depot at index
     */
    private Depot getNormalDepot(int index) throws IndexOutOfBoundsException{
        return depots.get(index);
    }

    public ArrayList<Depot> getDepotsLeader() {
        return depotsLeader;
    }

    /**
     * Return a leader depot
     * @param index of the depot i want
     * @return the leader depot at index*/
    private Depot getLeaderDepot(int index) throws IndexOutOfBoundsException{
        return depotsLeader.get(index);
    }


    /**
     * Return a depot
     * @param index of he depot i want
     * @param isNormalDepot true if normal, false if leader
     * @return the depot at index
     * */
    public Depot getDepot(int index, boolean isNormalDepot){
        if (isNormalDepot)
            return getNormalDepot(index);
        else
            return getLeaderDepot(index);
    }

    /**
     * Return the conversion of normal depot in ResourceData ArrayList
     * @return the conversion of normal depot in ResourceData ArrayList
     * */
    public ArrayList<ResourceData> toStandardDepotData(){
        ArrayList<ResourceData> standardDepots = new ArrayList<>();
        for (Depot depot: depots){
            standardDepots.add(depot.getResource().toClient());
        }
        return standardDepots;
    }

    /**
     * Return the conversion of leader depot in ResourceData ArrayList
     * @return the conversion of leader depot in ResourceData ArrayList
     * */
    public ArrayList<ResourceData> toLeaderDepotData(){
        ArrayList<ResourceData> leaderDepots = new ArrayList<>();
        for (Depot depot: depotsLeader){
            leaderDepots.add(depot.getResource().toClient());
        }
        return leaderDepots;
    }

    /**
     * Return the ArrayList of maxStorable
     * @return the ArrayList of maxStorable
     * */
    public ArrayList<Integer> toLeaderDepotMax(){
        ArrayList<Integer> maxStorage = new ArrayList<>();
        for (Depot depot: depotsLeader){
            maxStorage.add(depot.getMaxStorable());
        }
        return maxStorage;
    }
}
