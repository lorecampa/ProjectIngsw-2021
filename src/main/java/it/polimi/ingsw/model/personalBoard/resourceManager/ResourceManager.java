package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.observer.*;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class ResourceManager extends GameMasterObservable implements Observable<ResourceManagerObserver> {
    List<ResourceManagerObserver> resourceManagerObserverList = new ArrayList<>();

    private final Warehouse currWarehouse;
    private final Strongbox strongbox;
    private ArrayList<Resource> resourcesBuffer = new ArrayList<>();
    private final ArrayList<Resource> discounts=new ArrayList<>();
    private final ArrayList<Resource> resourcesToProduce=new ArrayList<>();

    private int faithPoint=0;
    private int anyRequired =0;
    private int anyToProduce = 0;

    private final ArrayList<Resource> myResources = new ArrayList<>();
    private final ArrayList<Resource> myDiscounts = new ArrayList<>();

    public ResourceManager(){
        currWarehouse = new Warehouse();
        strongbox = new Strongbox();
    }

    /**
     * Set up the resource manager to be ready for the curr turn
     * */
    public void newTurn(){
        anyRequired =0;
        anyToProduce = 0;
        faithPoint=0;
        resourcesBuffer.clear();
        resourcesToProduce.clear();
        allMyResources();
        myDiscounts.clear();
    }



    /**
     * Convert a list of resources to a list of concrete resources, remove ANY and FAITH
     * @param resourcesSent the original list I'll change
     */


    private ArrayList<Resource> fromResourceToConcreteResource(ArrayList<Resource> resourcesSent,
                                                               boolean countAnyProductionCost,
                                                               boolean countAnyProductionProfit,
                                                               boolean countFaithPoints){

        Resource resourceAny = ResourceFactory.createResource(ResourceType.ANY, 0);
        Resource resourceFaith = ResourceFactory.createResource(ResourceType.FAITH, 0);

        while(resourcesSent.contains(resourceAny)){
            if (countAnyProductionCost){
                anyRequired += resourcesSent.get(resourcesSent.indexOf(resourceAny)).getValue();
            }else if (countAnyProductionProfit){
                anyToProduce += resourcesSent.get(resourcesSent.indexOf(resourceAny)).getValue();
            }
            resourcesSent.remove(resourceAny);
        }

        while(resourcesSent.contains(resourceFaith)){
            if(countFaithPoints){
                faithPoint+=resourcesSent.get(resourcesSent.indexOf(resourceFaith)).getValue();
            }
            resourcesSent.remove(resourceFaith);
        }

        return resourcesSent;
    }

    public void convertAnyRequirement(ArrayList<Resource> resources, boolean isBuyDev) throws NegativeResourceException, AnyConversionNotPossible {
        for(Resource res: resources){
            if (myResources.contains(res)){
                myResources.get(myResources.indexOf(res)).subValue(res.getValue());
            }else{
                throw new AnyConversionNotPossible("You can't convert this any");
            }
        }
        resources.forEach(this::addToBuffer);
        anyRequired -= resources.stream().mapToInt(Resource::getValue).sum();

        if (anyRequired == 0){
            if (isBuyDev){
                notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.WAREHOUSE_RESOURCE_REMOVING));
            }else{
                if(anyToProduce != 0){
                    notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.PRODUCTION_ACTION));
                }else{
                    notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.ANY_PRODUCE_PROFIT_CONVERSION));
                }
            }
        }

    }

    public void convertAnyProductionProfit(ArrayList<Resource> resources){
        addToResourcesToProduce(resources, false, false);
        anyToProduce -= resources.stream().mapToInt(Resource::getValue).sum();

        if(anyToProduce == 0){
            notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.WAREHOUSE_RESOURCE_REMOVING));
        }
    }


    /**
     * Store the resources i have to manage from the market in the buffer
     * @param resourcesSent contain the array of the resources i got from market*/
    public void resourceFromMarket(ArrayList<Resource> resourcesSent){
        resourcesBuffer = fromResourceToConcreteResource(resourcesSent,
                false,
                false,
                true);

        notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.DEPOTS_POSITIONING));

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

        notifyAllObservers(x -> x.depotModify(resource, index, true));
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

        notifyAllObservers(x -> x.strongboxModify(resource, true));
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
        notifyAllObservers(x -> x.depotModify(resource, index, false));
    }

    /**
     * Subtract to the strongbox the resource
     * @param resource i want to subtract */
    public void subToStrongbox(Resource resource) throws NegativeResourceException {
        strongbox.subResourceValueOf(resource);

        notifyAllObservers(x -> x.strongboxModify(resource, false));
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
     * @param resources I want to add */
    public void addToResourcesToProduce(ArrayList<Resource> resources, boolean countAny, boolean countFaith) {

        fromResourceToConcreteResource(resources, false, countAny, countFaith);
        for (Resource res: resources){
            if(resourcesToProduce.contains(res)){
                resourcesToProduce.get(resourcesToProduce.indexOf(res)).addValue(res.getValue());
            }else{
                resourcesToProduce.add(res);
            }
        }


    }

    /**
     * Make the discount calculation based on the resource u are trying to have a discount with
     * @param res you want to have a discount with
     * */
    private void discount(Resource res){
        int valueDiscount;
        if(myDiscounts.contains(res)){
            try{
                valueDiscount = myDiscounts.get(myDiscounts.indexOf(res)).getValue();
                res.subValue(valueDiscount);
                myDiscounts.remove(res);
            }catch(NegativeResourceException e){
                valueDiscount = res.getValue();
                res.setValueToZero();
                try {
                    myDiscounts.get(myDiscounts.indexOf(res)).subValue(valueDiscount);
                } catch (NegativeResourceException negativeResourceException) {
                    //it will never happen because res.getValue is less than the first valueDiscount
                }
            }
        }
    }



    /**
     * Compute if u can afford some resources
     * @param resources i would like to be able to afford
     * @param checkDiscount if u want you resource to be discounted
     * @return true if u can false if u can't
     * */
    public boolean canIAfford(ArrayList<Resource> resources, boolean checkDiscount){

        int extraRes = numberOfResource() - numberOfResourceInBuffer();
        fromResourceToConcreteResource(resources,
                true,
                false,
                false);

        if (checkDiscount){
            allMyDiscounts();
        }
        for(Resource res : resources){
            if (myResources.contains(res)){
                discount(res);
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

        int numOfDiscountNotUsed =  myDiscounts.stream().mapToInt(Resource::getValue).sum();

        if(extraRes + numOfDiscountNotUsed < anyRequired)
            return false;

        for(Resource res : resources){
            addToBuffer(res);
        }


        if (anyRequired > 0){
            if(checkDiscount){
                notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.ANY_BUY_DEV_CONVERSION));
            }else{
                notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.ANY_PRODUCE_COST_CONVERSION));
            }
            notifyAllObservers(x -> x.anyConversionRequest(myResources, myDiscounts,
                    anyRequired, false));
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

    private void allMyDiscounts(){
        for (Resource discount: discounts){
            Resource discountCopy = ResourceFactory.createResource(discount.getType(), discount.getValue());
            myDiscounts.add(discountCopy);
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
   public void switchResourceFromDepotToDepot(int fromDepot,
                                              int toDepot, boolean isToLeader) throws TooMuchResourceDepotException, InvalidOrganizationWarehouseException {

       if (isToLeader &&
           currWarehouse.getDepot(fromDepot).getResourceType().
                   equals(currWarehouse.getDepotLeader(toDepot).getResourceType())){

           int toInsert = currWarehouse.getDepot(fromDepot).getResource().getValue();

           int freeSpace = currWarehouse.getDepotLeader(toDepot)
                   .howMuchResCanIStillStoreIn();


           int delta = freeSpace - toInsert;
           if (delta < 0)
               delta = freeSpace;
           else
               delta = toInsert;

           try {
               currWarehouse.getDepot(fromDepot).subValueResource(delta);
           } catch (NegativeResourceException e) {
               //it will never happen
           }

           currWarehouse.getDepotLeader(toDepot).addValueResource(delta);

       }else{
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

       notifyAllObservers(x -> x.depotSwitch(fromDepot, toDepot, isToLeader));
   }

    /**
     * Discard resources  called by controller CLEARBUFFER MESSAGE methods*/
    public void discardResources(){
        notifyGameMasterObserver(x -> x.discardResources(numberOfResourceInBuffer()));
        resourcesBuffer.clear();
    }

    /**Add a depot as a leaderDepot in the warehouse
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
    public void print(){
        currWarehouse.print();
        strongbox.print();
    }

    /**
     * Clear all the buffers in the resource manager, resourceToProduce and resourcesBuffer*/
    public void clearBuffers(){
        anyRequired = 0;
        resourcesToProduce.clear();
        resourcesBuffer.clear();
    }


    public int getFaithPoint() {
        return faithPoint;
    }

    public int getAnyRequired() {
        return anyRequired;
    }

    public Strongbox getStrongbox() {
        return strongbox;
    }

    @Override
    public void attachObserver(ResourceManagerObserver observer) {
        resourceManagerObserverList.add(observer);
    }

    @Override
    public void notifyAllObservers(Consumer<ResourceManagerObserver> consumer) {
        resourceManagerObserverList.forEach(consumer);
    }
}
