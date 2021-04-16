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
    private int faithPoint=0;
    private int anyResource=0;

    private final ArrayList<Resource> myResources = new ArrayList<>();

    public ResourceManager(){
        currWarehouse =new Warehouse();
        strongbox=new Strongbox();
    }

    /**
     * Set up the resource manager to be ready for the curr turn
     * */
    public void newTurn(){
        anyResource=0;
        faithPoint=0;
        resourcesBuffer.clear();
        resourcesToProduce.clear();
        allMyResources();
    }

    /**
     * Convert a list of resources to a list of concrete resources, remove ANY and FAITH
     * @param resourcesSent the original list I'll change
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
     * @param index of the depot i want to access (0 -> 1 res), (1 -> 2 res), (2 -> 3 res)
     * @param resource i want to add to that specific depot
     * @throws TooMuchResourceDepotException if i'm trying to add too much resource to that depot
     * @throws InvalidOrganizationWarehouseException if i'm trying to add a resource to one depot when there's another one with the same type*/
    public void addToWarehouse(boolean normalDepot, int index, Resource resource) throws TooMuchResourceDepotException, InvalidOrganizationWarehouseException {
        if(normalDepot){
            currWarehouse.addToStandardDepotValueAt(index, resource);
        }
        else{
            currWarehouse.addToLeaderDepotValueAt(index, resource);
        }
    }

    /**
     * Add all the resource store in resourcesToProduce to the strongbox
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
     * @throws InvalidOrganizationWarehouseException if i'm trying to add a resource to one depot whene there's onther one with the same type
     * @throws NegativeResourceException if the value of the resource in depot goes under 0*/
    public void subToWarehouse(boolean normalDepot, int index, Resource resource) throws InvalidOrganizationWarehouseException, NegativeResourceException {
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
    public void subToStrongbox(Resource resource) throws NegativeResourceException {
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

    /**
     * Make the discount calculation based on the resource u are trying to have a discount with
     * @param res you want to have a discount with*/
    private int discount(Resource res){
        int valueDiscount = 0;
        if(discounts.contains(res)){
            try{
                valueDiscount = discounts.get(discounts.indexOf(res)).getValue();
                res.subValue(valueDiscount);
            }catch(NegativeResourceException e){
                valueDiscount = res.getValue();
                res.setValueToZero();
            }
        }
        return valueDiscount;
    }

    public int numOfDiscounts(){
        int value = 0;
        for (Resource discount: discounts){
            value += discount.getValue();
        }
        return value;
    }

    /**
     * Compute if u can afford some resources
     * @param resources i would like to be able to afford
     * @param checkDiscount if u want you resource to be discounted
     * @return true if u can false if u can't
     * */
    public boolean canIAfford(ArrayList<Resource> resources, boolean checkDiscount){
        int extraRes = numberOfResource() - numberOfResourceInBuffer();
        fromResourceToConcreteResource(resources);
        int numOfAllDiscount = numOfDiscounts();
        for(Resource res : resources){
            if (myResources.contains(res)){
                if (checkDiscount)
                    numOfAllDiscount -= discount(res);
                try {
                    myResources.get(myResources.indexOf(res)).subValue(res.getValue());
                    extraRes -=  res.getValue();
                } catch (NegativeResourceException e) {
                    return false;
                }
            }
            else
                return false;
        }

        if((extraRes < anyResource && !checkDiscount) ||
                (extraRes + numOfAllDiscount < anyResource && checkDiscount))
            return false;


        for(Resource res : resources){
            addToBuffer(res);
        }
        return true;
    }

    /**
     * Store all the resource i own (strongbox + warehouse) in myResources arrayList*/
    private void allMyResources(){
        myResources.clear();
        ArrayList<Resource> resources = ResourceFactory.createAllConcreteResource();
        for(Resource res : resources){
            res.addValue(currWarehouse.howManyDoIHave(res.getType()));
            res.addValue(strongbox.howManyDoIHave(res.getType()));
            myResources.add(res);
        }
    }

    /**
     * Calculate the value of resources I'm storing in my warehouse + strongbox
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
     * Calculate the value of resources I'm storing in the resourcesBuffer
     * @return the value of resources i own in resourcesBuffer*/
    private int numberOfResourceInBuffer(){
        int value=0;
        for(Resource res : resourcesBuffer){
            value+=res.getValue();
        }
        return value;
    }

   /**
    * Switch the resource from fromDepot to toDepot
    * @param fromDepot the first depot
    * @param toDepot the second depot
    * */
   public void switchResourceFromDepotToDepot(int fromDepot, int toDepot) throws TooMuchResourceDepotException, InvalidOrganizationWarehouseException {
       Resource fromSupportResource = currWarehouse.removeResourceAt(fromDepot);
       Resource toSupportResource = currWarehouse.removeResourceAt(toDepot);
       try{
           currWarehouse.setResourceDepotAt(fromDepot, toSupportResource);
       }
       catch(TooMuchResourceDepotException | InvalidOrganizationWarehouseException e){
           currWarehouse.setResourceDepotAt(fromDepot, fromSupportResource);
           currWarehouse.setResourceDepotAt(toDepot, toSupportResource);
           throw e;
       }
       try{
           currWarehouse.setResourceDepotAt(toDepot, fromSupportResource);
       }
       catch(TooMuchResourceDepotException | InvalidOrganizationWarehouseException e){
           currWarehouse.setResourceDepotAt(fromDepot, fromSupportResource);
           currWarehouse.setResourceDepotAt(toDepot, toSupportResource);
           throw e;
       }
   }

    /**
     * Discard resources*/
    public void discardResources(){
        notifyAllObservers();
        resourcesBuffer.clear();
    }

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
     * Clear all the buffers in the resource manager, resourceToProduce and resourcesBuffer*/
    public void clearBuffers(){
        anyResource = 0;
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

    public void print(){
        System.out.println("ANY: "+anyResource+"-"+" FAITH: "+faithPoint);
        currWarehouse.print();
        strongbox.print();
    }

    public int getFaithPoint() {
        return faithPoint;
    }

    public int getAnyResource() {
        return anyResource;
    }

    public Strongbox getStrongbox() {
        return strongbox;
    }
}
