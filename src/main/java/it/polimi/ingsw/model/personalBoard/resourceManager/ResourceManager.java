package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.model.PlayerState;
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
    private final ArrayList<Resource> resourcesBuffer = new ArrayList<>();
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
    public void restoreRM(){
        anyRequired =0;
        anyToProduce = 0;
        faithPoint=0;
        resourcesBuffer.clear();
        resourcesToProduce.clear();
        allMyResources();
        allMyDiscounts();
    }

    public Warehouse getWarehouse() {
        return currWarehouse;
    }

    /**
     * Convert a list of resources to a list of concrete resources, remove ANY and FAITH
     * @param resourcesSent the original list I'll change
     */

    private void fromResourceToConcreteResource(ArrayList<Resource> resourcesSent, boolean countAnyProductionCost, boolean countAnyProductionProfit, boolean countFaithPoints){

        int any = resourcesSent.stream().filter(x -> x.getType() == ResourceType.ANY).mapToInt(Resource::getValue).sum();
        if(countAnyProductionCost) anyRequired += any;
        else if (countAnyProductionProfit) anyToProduce += any;

        int faith = resourcesSent.stream().filter(x -> x.getType() == ResourceType.FAITH).mapToInt(Resource::getValue).sum();
        if (countFaithPoints) faithPoint += faith;

        resourcesSent.removeIf(x -> (x.getType() == ResourceType.ANY) || (x.getType() == ResourceType.FAITH));

    }

    private boolean containsAnyOrFaith(ArrayList<Resource> resources) {
        return resources.stream()
                .anyMatch(x -> x.getType() == ResourceType.ANY || x.getType() == ResourceType.FAITH);
    }

    private void restoreMyResources(ArrayList<Resource> tempBuffer){
        tempBuffer.forEach(x -> myResources.get(myResources.indexOf(x)).addValue(x.getValue()));
    }

    public void convertAnyRequirement(ArrayList<Resource> resources, boolean isFromBuyDevelopment) throws AnyConversionNotPossible {
        if(containsAnyOrFaith(resources)){
            throw new AnyConversionNotPossible("Your response contains  any or faith, please try again");
        }
        int numOfConversion = resources.stream().mapToInt(Resource::getValue).sum();
        if (numOfConversion != anyRequired) {
            throw new AnyConversionNotPossible("Num of any requested to convert is different than the number of " +
                    "resources inserted");
        }

        ArrayList<Resource> tempBuffer = new ArrayList<>();
        for(Resource res: resources){
            if (myResources.contains(res)){
                try{
                    myResources.get(myResources.indexOf(res)).subValue(res.getValue());
                    tempBuffer.add(res);
                    anyRequired -= res.getValue();

                }catch (NegativeResourceException e){
                    restoreMyResources(tempBuffer);
                    anyRequired += tempBuffer.stream().mapToInt(Resource::getValue).sum();
                    throw new AnyConversionNotPossible("You can't convert this any, you don't have " +
                            "enough " + res.getType().getDisplayName() + ". Please try again!");
                }
            }else{
                restoreMyResources(tempBuffer);
                anyRequired += tempBuffer.stream().mapToInt(Resource::getValue).sum();
                throw new AnyConversionNotPossible("You can't convert this any, you don't own "
                        + res.getType().getDisplayName() + ". Please try again!");
            }
        }
        tempBuffer.forEach(this::addToBuffer);

        if (anyRequired == 0){
            if (isFromBuyDevelopment){
                notifyGameMaster(x -> x.onPlayerStateChange(PlayerState.BUY_DEV_RESOURCE_REMOVING));
                notifyAllObservers(x->x.warehouseRemovingRequest(resourcesBuffer));
            }else{
                if(anyToProduce > 0){
                    notifyGameMaster(x -> x.onPlayerStateChange(PlayerState.ANY_PRODUCE_PROFIT_CONVERSION));
                    notifyAllObservers(x -> x.anyProductionProfitRequest(anyToProduce));
                }else{
                    notifyGameMaster(x -> x.onPlayerStateChange(PlayerState.PRODUCTION_ACTION));
                    notifyAllObservers(ResourceManagerObserver::productionCardSelectionCompleted);
                }
            }
        }

    }



    public void convertAnyProductionProfit(ArrayList<Resource> resources) throws AnyConversionNotPossible {
        if(containsAnyOrFaith(resources)){
            throw new AnyConversionNotPossible("Your response contains  any or faith, please try again");
        }
        int numOfConversion = resources.stream().mapToInt(Resource::getValue).sum();
        if (anyToProduce != numOfConversion){
            throw new AnyConversionNotPossible("Num of any requested to convert is different than the number of " +
                    "resources inserted");
        }

        for (Resource res: resources){
            if(resourcesToProduce.contains(res)){
                resourcesToProduce.get(resourcesToProduce.indexOf(res)).addValue(res.getValue());
            }else{
                resourcesToProduce.add(res);
            }
        }
        anyToProduce -= numOfConversion;

        if(anyToProduce == 0){
            notifyGameMaster(x -> x.onPlayerStateChange(PlayerState.PRODUCTION_ACTION));
            notifyAllObservers(ResourceManagerObserver::productionCardSelectionCompleted);
        }
    }

    public void applyFaithPoints(){
        notifyGameMaster(x -> x.increasePlayerFaithPoint(faithPoint));
    }



    /**
     * Store the resources i have to manage from the market in the buffer
     * @param resourcesSent contain the array of the resources i got from market*/
    public void resourceFromMarket(ArrayList<Resource> resourcesSent){
        fromResourceToConcreteResource(resourcesSent, false, false,
                true);
        resourcesSent.forEach(this::addToBuffer);
        notifyAllObservers(x -> x.depotPositioningRequest(resourcesBuffer));
        notifyGameMaster(x -> x.onPlayerStateChange(PlayerState.MARKET_RESOURCE_POSITIONING));
    }


    /**
     * Add all the resource store in resourcesToProduce to the strongbox
    */
    public void doProduction(){
        resourcesToProduce.forEach(this::addToStrongbox);
        notifyAllObservers(x -> x.strongboxUpdate(strongbox.getResources()));
    }
    public void stopProduction() throws InvalidStateActionException {
        checkPlayerState(PlayerState.PRODUCTION_ACTION);
        notifyAllObservers(x -> x.warehouseRemovingRequest(resourcesBuffer));
        notifyGameMaster(x -> x.onPlayerStateChange(PlayerState.PRODUCTION_RESOURCE_REMOVING));
    }

    /**
     * Add to the strongbox the resource
     * @param resource i want to add */
    public void addToStrongbox(Resource resource){
        strongbox.addResource(resource);
    }

    /**
     * Subtract to the strongbox the resource
     * @param resource i want to subtract */
    public void subToStrongbox(Resource resource) throws NegativeResourceException, InvalidStateActionException {
        checkPlayerState(PlayerState.BUY_DEV_RESOURCE_REMOVING,
                PlayerState.PRODUCTION_RESOURCE_REMOVING);

        strongbox.subResource(resource);
        notifyAllObservers(x -> x.strongboxUpdate(strongbox.getResources()));
        sendBufferUpdate();
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
        sendBufferUpdate();
    }

    /**
     * Subtract to the warehouse the resource
     * @param isNormalDepot true for default false for leaderDepots
     * @param index of the depot i want to access
     * @param resource i want to subtract to that specific depot
     * @throws InvalidOrganizationWarehouseException if i'm trying to add a resource to one depot when there's other one with the same type
     * @throws NegativeResourceException if the value of the resource in depot goes under 0*/
    public void subToWarehouse(boolean isNormalDepot, int index, Resource resource) throws InvalidOrganizationWarehouseException, NegativeResourceException {
        currWarehouse.subDepotResourceAt(index, resource, isNormalDepot);
        sendDepotUpdate(isNormalDepot, index);
        sendBufferUpdate();
    }

    /**
     * Switch the resource from fromDepot to toDepot
     * @param fromIndex the first depot
     * @param toIndex the second depot
     * */
    public void switchResourceFromDepotToDepot(int fromIndex, boolean isFromNormalDepot,
                                               int toIndex, boolean isToNormalDepot) throws TooMuchResourceDepotException, InvalidOrganizationWarehouseException, InvalidStateActionException {
        checkPlayerState(PlayerState.BUY_DEV_RESOURCE_REMOVING,
                PlayerState.PRODUCTION_RESOURCE_REMOVING,
                PlayerState.MARKET_RESOURCE_POSITIONING);

        Resource from = currWarehouse.popResourceFromDepotAt(fromIndex, isFromNormalDepot);
        Resource to = currWarehouse.popResourceFromDepotAt(toIndex, isToNormalDepot);

        try{
            if(from.getType() != ResourceType.ANY && !(!isFromNormalDepot && from.getValue() == 0)){
                currWarehouse.addDepotResourceAt(toIndex, from, isToNormalDepot);
            }

            if(to.getType() != ResourceType.ANY && !(!isToNormalDepot && to.getValue() == 0)){
                currWarehouse.addDepotResourceAt(fromIndex, to, isFromNormalDepot);
            }
        }
        catch(Exception e){
            currWarehouse.restoreDepot(fromIndex, isFromNormalDepot);
            currWarehouse.addDepotResourceAt(fromIndex, from, isFromNormalDepot);

            currWarehouse.restoreDepot(toIndex, isToNormalDepot);
            currWarehouse.addDepotResourceAt(toIndex, to, isToNormalDepot);
            throw e;
        }

        sendDepotUpdate(isFromNormalDepot, fromIndex);
        sendDepotUpdate(isToNormalDepot, toIndex);
    }

    private void sendDepotUpdate(boolean isNormalDepot, int index){
        Depot depot = currWarehouse.getDepot(index, isNormalDepot);
        notifyAllObservers(x -> x.depotUpdate(depot.getResource(),index, isNormalDepot));
    }



    /**
     * Used to add a resource value or the resource itself in the buffer
     * @param res I want to add from the buffer
     */
    public void addToBuffer(Resource res){
        if(resourcesBuffer.contains(res)){
            resourcesBuffer.get(resourcesBuffer.indexOf(res)).addValue(res.getValue());
        }
        else{
            resourcesBuffer.add(res);
        }
    }



    /**
     * Used to remove a resource value from the buffer in resource manager
     * @param resource I want to remove from the buffer
     * @throws NegativeResourceException if resource will go under value 0*/
    public void subToBuffer(Resource resource) throws Exception {
        if(resourcesBuffer.contains(resource)){
            int resourceIndex = resourcesBuffer.indexOf(resource);
            int delta = resourcesBuffer.get(resourceIndex).getValue() - resource.getValue();
            resourcesBuffer.get(resourceIndex).subValue(resource.getValue());
            if (delta == 0)
                resourcesBuffer.remove(resourceIndex);
        }else{
            throw new Exception("Resource not present in buffer");
        }

    }

    public int getBufferSize(){
        return resourcesBuffer.stream().mapToInt(Resource::getValue).sum();
    }

    /**
     * Used to add a resource value or the resource itself in the resource to produce
     * @param resources I want to add */
    public void addToResourcesToProduce(ArrayList<Resource> resources) {
        fromResourceToConcreteResource(resources, false, true,
                true);
        for (Resource res: resources){
            if(resourcesToProduce.contains(res)){
                resourcesToProduce.get(resourcesToProduce.indexOf(res)).addValue(res.getValue());
            }else{
                resourcesToProduce.add(res);
            }
        }

        if(anyRequired == 0 && anyToProduce == 0){
            notifyGameMaster(x -> x.onPlayerStateChange(PlayerState.PRODUCTION_ACTION));
        }else if (anyRequired == 0 && anyToProduce > 0){
            notifyGameMaster(x -> x.onPlayerStateChange(PlayerState.ANY_PRODUCE_PROFIT_CONVERSION));
            notifyAllObservers(x -> x.anyProductionProfitRequest(anyToProduce));
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
                } catch (Exception ignored) {
                    //it will never happen because res.getValue is less than the first valueDiscount
                }
            }
        }
    }



    /**
     * Compute if u can afford some resources
     * @param resources i would like to be able to afford
     * @param checkDiscount if u want you resource to be discounted
     * */

    public void canIAfford(ArrayList<Resource> resources, boolean checkDiscount) throws NotEnoughRequirementException {
        int extraRes = numberOfResource() - numberOfResourceInBuffer();
        fromResourceToConcreteResource(resources, true, false, false);

        ArrayList<Resource> tempBuffer = new ArrayList<>();
        for(Resource res : resources){
            if (myResources.contains(res)){
                if(checkDiscount) discount(res);
                try {
                    myResources.get(myResources.indexOf(res)).subValue(res.getValue());
                    tempBuffer.add(res);
                    extraRes -=  res.getValue();
                } catch (NegativeResourceException e) {
                    restoreMyResources(tempBuffer);
                    throw new NotEnoughRequirementException("You don't have enough " + res.getType());

                }
            }
            else{
                restoreMyResources(tempBuffer);
                throw new NotEnoughRequirementException("You don't have " + res.getType());
            }

        }

        int numOfDiscountNotUsed =  myDiscounts.stream().mapToInt(Resource::getValue).sum();

        if(extraRes + numOfDiscountNotUsed < anyRequired){
            restoreMyResources(tempBuffer);
            throw new NotEnoughRequirementException("You don't have enough resources to try to " +
                    "transform the any resources required in your card");
        }
        resources.forEach(this::addToBuffer);

        if(anyRequired == 0){
            if (checkDiscount){
                notifyGameMaster(x -> x.onPlayerStateChange(PlayerState.BUY_DEV_RESOURCE_REMOVING));
                notifyAllObservers(x-> x.warehouseRemovingRequest(resourcesBuffer));
            }else{
                notifyAllObservers(ResourceManagerObserver::productionCardSelectionCompleted);
            }
        }else if (anyRequired > 0){
            if(checkDiscount){
                notifyGameMaster(x -> x.onPlayerStateChange(PlayerState.ANY_BUY_DEV_CONVERSION));
            }else{
                notifyGameMaster(x -> x.onPlayerStateChange(PlayerState.ANY_PRODUCE_COST_CONVERSION));
            }
            notifyAllObservers(x -> x.anyRequirementConversionRequest(myResources, myDiscounts, anyRequired));
        }


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
        myDiscounts.clear();
        for (Resource discount: discounts){
            myDiscounts.add(ResourceFactory.createResource(discount.getType(), discount.getValue()));
        }
    }


    /**
     * Calculate the value of resources I'm storing in my warehouse + strongbox
     * @return the value of resources i own*/
    private int numberOfResource(){
        int value=0;
        ArrayList<Resource> resources = ResourceFactory.createAllConcreteResource();
        for(Resource res : resources){
            value += currWarehouse.howManyDoIHave(res.getType());
            value += strongbox.howManyDoIHave(res.getType());
        }
        return value;
    }

    /**
     * Calculate the value of resources I'm storing in the resourcesBuffer
     * @return the value of resources i own in resourcesBuffer*/
    private int numberOfResourceInBuffer(){
        return resourcesBuffer.stream().mapToInt(Resource::getValue).sum();
    }


    /**
     * Discard resources  that you don't want to place*/
    public void discardResourcesFromMarket() throws InvalidStateActionException {
        checkPlayerState(PlayerState.MARKET_RESOURCE_POSITIONING);

        notifyGameMaster(x -> x.discardResources(numberOfResourceInBuffer()));
        resourcesBuffer.clear();
        sendBufferUpdate();
    }

    public void sendBufferUpdate(){
        notifyAllObservers(x -> x.bufferUpdate(resourcesBuffer));
    }

    public int getVictoryPointsResource(){
        int numRes = myResources.stream().mapToInt(Resource::getValue).sum();
        return Math.floorDiv(numRes, 5);
    }


    /**Add a depot as a leaderDepot in the warehouse
     * @param depots i want to add*/
    public void addLeaderDepot(ArrayList<Depot> depots){
        for(Depot dep : depots){
            currWarehouse.addDepotLeader(dep);
        }
        notifyAllObservers(x -> x.updateLeaderDepot(depots, false));
    }

    public void removeLeaderDepot(ArrayList<Depot> depots){
        for(Depot dep: depots){
            currWarehouse.removeDepotLeader(dep);
        }
        notifyAllObservers(x -> x.updateLeaderDepot(depots, true));
    }



    /**
     * Add resource to the list of discount i have
     * @param cardDiscounts that i have "discount"*/
    public void addDiscount(ArrayList<Resource> cardDiscounts) {
        for(Resource dis : cardDiscounts){
            if(discounts.contains(dis)){
                discounts.get(discounts.indexOf(dis)).addValue(dis.getValue());
            }
            else{
                discounts.add(dis);
            }
        }
    }

    public void removeDiscount(ArrayList<Resource> cardDiscounts){
        for(Resource dis: cardDiscounts){
            if (discounts.contains(dis)){
                try {
                    discounts.get(discounts.indexOf(dis)).subValue(dis.getValue());
                } catch (Exception ignored) {
                }
            }
        }
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
