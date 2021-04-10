package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.commonInterfaces.Observable;
import it.polimi.ingsw.commonInterfaces.Observer;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;

public class ResourceManager implements Observable {
    private final Warehouse currWarehouse;
    private final Strongbox strongbox;
    private ArrayList<Resource> resourcesBuffer = new ArrayList<>();
    private final ArrayList<Resource> discounts=new ArrayList<>();
    private final ArrayList<Resource> resourcesToProduce=new ArrayList<>();
    private final ArrayList<Observer> observers = new ArrayList<>();
    private Resource supportResource; //support perchè sarebbe una "variabile d'appoggio"
    private int faithPoint=0;
    private int anyResource=0;

    private ArrayList<Resource> myResources = new ArrayList<>();

    public ResourceManager(){
        currWarehouse =new Warehouse();
        strongbox=new Strongbox();
    }

    //set up per valori il prossimo turno
    /**
     * Set up the resource manager to be ready for the curr turn*/
    public void newTurn(){
        anyResource=0;
        faithPoint=0;
        resourcesBuffer.clear();
        resourcesToProduce.clear();
        allMyResources();
    }

    //Metodi specifici azione 3 produzione mercato (player preme su azione 3 va al mercato mi ritorna un vettore da mettere nella warehouse)

    /**
     * Convert a list of resources to a list of concrete resources, remove ANY and FAITH
     * @param resourcesSent: the original list
     */
    private ArrayList<Resource> fromResourceToConcreteResource(ArrayList<Resource> resourcesSent){
        Resource resourceAny = ResourceFactory.createResource(ResourceType.ANY, 0);
        Resource resourceFaith = ResourceFactory.createResource(ResourceType.FAITH, 0);
        while(resourcesSent.contains(resourceAny)){
            anyResource+= resourcesSent.get(resourcesSent.indexOf(resourceAny)).getValue();
            resourcesSent.remove(resourceAny);
        }
        while(resourcesSent.contains(resourceFaith)){
            faithPoint+=resourcesSent.get(resourcesSent.indexOf(resourceFaith)).getValue();
            resourcesSent.remove(resourceFaith);
        }
        return resourcesSent;
    }

    /**
     * Store the resources i have to manage from the market in the buffer
     * @param resourcesSent contain the array of the resources i got from market*/
    public void resourceFromMarket(ArrayList<Resource> resourcesSent){
        resourcesBuffer = fromResourceToConcreteResource(resourcesSent);
    }

    /**
     * Add to the warehouse the resource
     * @param normalDepot true for default false for leaderDepots
     * @param index of the depot i want to access
     * @param resource i want to add to that specific depot
     * @throws TooMuchResourceDepotException if i'm trying to add too much resource to that depot
     * @throws InvalidOrganizationWarehouseException if i'm trying to add a resource to one depot whene there's onther one with the same type
     * @throws CantModifyDepotException
     * @throws NegativeResourceException if the value of the resource in depot goes under 0*/
    public void addToWarehouse(boolean normalDepot, int index, Resource resource) throws TooMuchResourceDepotException, InvalidOrganizationWarehouseException, CantModifyDepotException {
        if(normalDepot){
            currWarehouse.addToStandardDepotValueAt(index, resource);
        }
        else{
            currWarehouse.addToLeaderDepotValueAt(index, resource);
        }
    }

    /**
     * Add all the resource store in resourcesToProduce to the stronbox
    */
    public void doProduction(){
        for(Resource res:resourcesToProduce){
            addToStrongbox(res);
        }
    }

    /**
     * Add to the strongbox the resource
     * @param resource i want to add */
    public void addToStrongbox(Resource resource){
        strongbox.addResourceValueOf(resource);
    }

    /**
     * Subtract to the warehouse the resource
     * @param normalDepot true for default false for leaderDepots
     * @param index of the depot i want to access
     * @param resource i want to subtract to that specific depot
     * @throws TooMuchResourceDepotException if i'm trying to add too much resource to that depot
     * @throws InvalidOrganizationWarehouseException if i'm trying to add a resource to one depot whene there's onther one with the same type
     * @throws CantModifyDepotException
     * @throws NegativeResourceException if the value of the resource in depot goes under 0*/
    public void subtractToWarehouse(boolean normalDepot, int index, Resource resource) throws TooMuchResourceDepotException, InvalidOrganizationWarehouseException, CantModifyDepotException, NegativeResourceException {
        if(normalDepot){
            currWarehouse.subToStandardDepotValueAt(index, resource);
        }
        else{
            currWarehouse.subToLeaderDepotValueAt(index, resource);
        }
    }

    /**
     * Subtract to the strongbox the resource
     * @param resource i want to subtract */
    public void subtractToStrongbox(Resource resource) throws NegativeResourceException {
        strongbox.subResourceValueOf(resource);
    }

    /**
     * Convert one ANY to a specific resource
     * @param type of resource i want to have
     * @return the resource i'm asking for*/
    private Resource changeAnyInResource(ResourceType type, boolean isCost) throws NoMoreAnyResourceException, NegativeResourceException {
        if(anyResource <= 0){ //maybe
            throw new NoMoreAnyResourceException("don't have enough any");
        }
        Resource resource = ResourceFactory.createResource(type,1);
        if (isCost){
            if (!myResources.contains(resource))
                throw new NegativeResourceException("don't have this resource");
            else
                myResources.get(myResources.indexOf(resource)).subValue(1);
        }
        anyResource--;
        return resource;
    }


    /**
     * Used to add a resource value or the resource itself in the buffer
     * @param resource I want to add from the buffer
     */
    public void addToBuffer(Resource resource){
        if(resourcesBuffer.contains(resource)){
            resourcesBuffer.get(resourcesBuffer.indexOf(resource)).addValue(resource.getValue());
        }
        else{
            resourcesBuffer.add(resource);
        }
    }

    /**
     * Used to remove a resource value from the buffer in resource manager
     * @param resource I want to remove from the buffer
     * @throws NegativeResourceException if resource'll go under value 0*/
    public void subtractToBuffer(Resource resource) throws NegativeResourceException {
        if(resourcesBuffer.contains(resource)){
            resourcesBuffer.get(resourcesBuffer.indexOf(resource)).subValue(resource.getValue());
        }
    }

    /**
     * Used to add a resource value or the resource itself in the resource to produce
     * @param resource I want to add */
    public void addToResourcesToProduce(Resource resource) {
        if(resourcesToProduce.contains(resource)){
            resourcesToProduce.get(resourcesToProduce.indexOf(resource)).addValue(resource.getValue());
        }
        else{
            resourcesToProduce.add(resource);
        }
    }

    private void discount(Resource res){
        if(discounts.contains(res)){
            try{
                res.subValue(discounts.get(discounts.indexOf(res)).getValue());
            }catch(NegativeResourceException e){
                res.setValueToZero();
            }
        }
    }

    /**
     * Compute if u can afford some resources
     * @param resources i would like to be able to afford
     * @param checkDiscount if u want you resource to be discounted
     * @return true if u can false if u can't*/
    public boolean canIAfford(ArrayList<Resource> resources, boolean checkDiscount){
        int extraRes = numberOfResource() - numberOfResourceInBuffer();
        fromResourceToConcreteResource(resources);
        for(Resource res : resources){
            if (myResources.contains(res)){
                if (checkDiscount)
                    discount(res);
                try {
                    myResources.get(myResources.indexOf(res)).subValue(res.getValue());
                    extraRes -=  res.getValue();
                    if(myResources.get(myResources.indexOf(res)).getValue() == 0)
                        myResources.remove(res);
                } catch (NegativeResourceException e) {
                    return false;
                }
            }
            else
                return false;
        }

        if(extraRes < anyResource)
            return false;

        for(Resource res : resources){
            addToBuffer(res);
        }
        return true;
    }

    private void allMyResources(){
        ArrayList<Resource> resources = ResourceFactory.createAllConcreteResource();
        for(Resource res : resources){
            res.addValue(currWarehouse.howManyDoIHave(res.getType()));
            res.addValue(strongbox.howManyDoIHave(res.getType()));
            if (res.getValue() != 0)
                myResources.add(res);
        }
    }

    /**
     *
     * @return the value of resources i own*/
    private int numberOfResource(){
        int value=0;
        ArrayList<Resource> resources = ResourceFactory.createAllConcreteResource();
        for(Resource res : resources){
            value+= currWarehouse.howManyDoIHave(res.getType());
            value+= strongbox.howManyDoIHave(res.getType());
        }
        return value;
    }

    /**
     *
     * @return the value of resources i own in resourcesBuffer*/
    private int numberOfResourceInBuffer(){
        int value=0;
        for(Resource res : resourcesBuffer){
            value+=res.getValue();
        }
        return value;
    }

   /**
    * switch the resource from fromDepot and toDepot
    * @param fromDepot the first depot
    * @param toDepot the second depot
    * */
   public void switchResourceFromDepotToDepot(int fromDepot, int toDepot) throws CantModifyDepotException, TooMuchResourceDepotException, InvalidOrganizationWarehouseException {
       supportResource = currWarehouse.removeResourceAt(fromDepot);
       currWarehouse.setResourceDepotAt(fromDepot, currWarehouse.getDepot(toDepot).getResource());
       try{
           currWarehouse.setResourceDepotAt(toDepot, supportResource);
       }
       catch(CantModifyDepotException | TooMuchResourceDepotException | InvalidOrganizationWarehouseException e){
           currWarehouse.setResourceDepotAt(fromDepot, supportResource);
           throw e;
       }
   }

    /**
     * Discard res*/
    public void discardResources(){
        notifyAllObservers();
        resourcesBuffer.clear();
    }

    //metodi per i leader
    /**Add a depot as a leaderDepot in the wearehouse
     * @param depot i want to add*/
    public void addLeaderDepot(Depot depot){
        currWarehouse.addDepotLeader(depot);
    }

    /**
     * Add resource to the list of discount i have
     * @param resource that i have "discount"*/
    public void addDiscount(Resource resource) {
        if(discounts.contains(resource)){
            discounts.get(discounts.indexOf(resource)).addValue(resource.getValue());
        }
        else{
            discounts.add(resource);
        }
    }

    /**
     * Clear all the buffers in the resource manager*/
    public void clearBuffers(){
        resourcesToProduce.clear();
        resourcesBuffer.clear();
    }

    @Override
    public void attachObserver(Observer observer) {
        if (!observers.contains(observer))
            observers.add(observer);
    }

    @Override
    public void notifyAllObservers() {
        for (Observer obs : observers)
            obs.updateFromResourceManager(numberOfResourceInBuffer());
    }

    //metodo per me
    public void print(){
        System.out.println("ANY: "+anyResource+"-"+" FAITH: "+faithPoint);
        currWarehouse.print();
        strongbox.print();
    }

}
