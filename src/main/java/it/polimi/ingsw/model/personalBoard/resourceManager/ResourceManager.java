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

    private void containsAnyOrFaith(ArrayList<Resource> resources) throws AnyConversionNotPossible {
        if(resources.stream()
                .anyMatch(x -> x.getType() == ResourceType.ANY || x.getType() == ResourceType.FAITH)){
            throw new AnyConversionNotPossible("Your response contains  any or faith, please try again");
        }
    }

    public void convertAnyRequirement(ArrayList<Resource> resources, boolean isFromBuyDevelopment) throws AnyConversionNotPossible {
        containsAnyOrFaith(resources);
        int numOfConversion = resources.stream().mapToInt(Resource::getValue).sum();
        if (numOfConversion > anyRequired) {
            throw new AnyConversionNotPossible("Num of any requested to convert is less than the number inserted");
        }

        for(Resource res: resources){
            if (myResources.contains(res)){
                try{
                    myResources.get(myResources.indexOf(res)).subValue(res.getValue());
                }catch (NegativeResourceException e){
                    throw new AnyConversionNotPossible("You can't convert this any, you don't have " +
                            "enough " + res.getType());
                }
            }else{
                throw new AnyConversionNotPossible("You can't convert this any, you don't own " + res.getType());
            }
        }
        resources.forEach(this::addToBuffer);

        anyRequired -= numOfConversion;

        if (anyRequired == 0){
            if (isFromBuyDevelopment){
                notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.WAREHOUSE_RESOURCE_REMOVING));
            }else{
                if(anyToProduce > 0){
                    notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.ANY_PRODUCE_PROFIT_CONVERSION));
                }else{
                    notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.PRODUCTION_ACTION));
                }
            }
        }
    }

    public void convertAnyProductionProfit(ArrayList<Resource> resources) throws AnyConversionNotPossible {
        containsAnyOrFaith(resources);
        int numOfConversion = resources.stream().mapToInt(Resource::getValue).sum();
        if (anyToProduce < numOfConversion){
            throw new AnyConversionNotPossible("Num of any requested to convert is less than the number inserted");
        }

        addToResourcesToProduce(resources, false, false);
        anyToProduce -= numOfConversion;

        if(anyToProduce == 0){
            notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.PRODUCTION_ACTION));
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
        strongbox.addResource(resource);
        //TODO change
        notifyAllObservers(x -> x.strongboxUpdate(strongbox.getResources()));
    }

    /**
     * Subtract to the strongbox the resource
     * @param resource i want to subtract */
    public void subToStrongbox(Resource resource) throws NegativeResourceException {
        strongbox.subResource(resource);

        notifyAllObservers(x -> x.strongboxUpdate(strongbox.getResources()));
    }

    /**
     * Add to the warehouse the resource
     * @param isNormalDepot true for default false for leaderDepots
     * @param index of the depot i want to access (0 -> 1 res), (1 -> 2 res), (2 -> 3 res)
     * @param resource i want to add to that specific depot
     * @throws TooMuchResourceDepotException if i'm trying to add too much resource to that depot
     * @throws InvalidOrganizationWarehouseException if i'm trying to add a resource to one depot when there's another one with the same type*/
    public void addToWarehouse(boolean isNormalDepot, int index, Resource resource) throws TooMuchResourceDepotException, InvalidOrganizationWarehouseException {
        currWarehouse.addDepotResourceAt(index, resource, isNormalDepot);
        sendDepotUpdate(isNormalDepot, index);
    }

    /**
     * Subtract to the warehouse the resource
     * @param isNormalDepot true for default false for leaderDepots
     * @param index of the depot i want to access
     * @param resource i want to subtract to that specific depot
     * @throws InvalidOrganizationWarehouseException if i'm trying to add a resource to one depot whene there's onther one with the same type
     * @throws NegativeResourceException if the value of the resource in depot goes under 0*/
    public void subToWarehouse(boolean isNormalDepot, int index, Resource resource) throws InvalidOrganizationWarehouseException, NegativeResourceException {
        currWarehouse.subDepotResourceAt(index, resource, isNormalDepot);
        sendDepotUpdate(isNormalDepot, index);



    }

    private void sendDepotUpdate(boolean isNormalDepot, int index){
        Depot depot;
        if (isNormalDepot){
            depot = currWarehouse.getDepot(index);
        }else{
            depot = currWarehouse.getDepotLeader(index);
        }

        notifyAllObservers(x -> x.depotUpdate(depot.getResource(),index, isNormalDepot));
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
        System.out.println(resourcesBuffer);
    }

    /**
     * Used to remove a resource value from the buffer in resource manager
     * @param resource I want to remove from the buffer
     * @throws NegativeResourceException if resource'll go under value 0*/
    public void subtractToBuffer(Resource resource) throws Exception {
        if(resourcesBuffer.contains(resource)){
            int resourceIndex = resourcesBuffer.indexOf(resource);
            int delta = resourcesBuffer.get(resourceIndex).getValue() - resource.getValue();
            resourcesBuffer.get(resourceIndex).subValue(resource.getValue());
            if (delta == 0){
                resourcesBuffer.remove(resourceIndex);
            }
            controlBufferStatus();

        }else{
            throw new Exception("Resource not present in buffer");
        }


        if (resourcesBuffer.size() == 0){
            notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.LEADER_MANAGE_AFTER));
        }
    }

    private void controlBufferStatus(){
        if (resourcesBuffer.size() == 0){
            notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.LEADER_MANAGE_AFTER));
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

    //TODO if we have time
    //instead of return true or false it would be better to throw a NotEnoughRequirementException
    //with a custom message with
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
   public void switchResourceFromDepotToDepot(int fromDepot, boolean isFromNormalDepot,
                                              int toDepot, boolean isToNormalDepot) throws TooMuchResourceDepotException, InvalidOrganizationWarehouseException {

       Resource fromSupportResource = currWarehouse.popResourceFromDepotAt(fromDepot, isFromNormalDepot);
       Resource toSupportResource = currWarehouse.popResourceFromDepotAt(toDepot, isToNormalDepot);

       try{
           currWarehouse.addDepotResourceAt(toDepot, fromSupportResource, isToNormalDepot);
       }
       catch(Exception e){
           currWarehouse.addDepotResourceAt(fromDepot, fromSupportResource, isFromNormalDepot);
           currWarehouse.addDepotResourceAt(toDepot, toSupportResource, isToNormalDepot);
           throw e;
       }
       try{
           currWarehouse.addDepotResourceAt(fromDepot, toSupportResource, isFromNormalDepot);
       }
       catch(Exception e){
           currWarehouse.addDepotResourceAt(fromDepot, fromSupportResource, isFromNormalDepot);
           currWarehouse.addDepotResourceAt(toDepot, toSupportResource, isToNormalDepot);
           throw e;
       }

       sendDepotUpdate(isFromNormalDepot, fromDepot);
       sendDepotUpdate(isToNormalDepot, toDepot);
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
