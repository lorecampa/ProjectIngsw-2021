package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;

public class ResourceManager {
    private Warehouse currWarehouse;
    private Strongbox strongbox;
    private ArrayList<Resource> resourcesBuffer = new ArrayList<>();
    private ArrayList<Resource> discounts=new ArrayList<>();
    private ArrayList<Resource> resourcesToProduce=new ArrayList<>();
    private Resource supportResource; //support perchè sarebbe una "variabile d'appoggio"
    private int faithPoint=0;
    private int anyResource=0;

    public ResourceManager(){
        currWarehouse =new Warehouse();
        strongbox=new Strongbox();
        resourcesBuffer=null;
    }

    //set up per il prossimo turno
    /**
     * Set up the resource manager to be ready for the curr turn*/
    public void newTurn(){
        anyResource=0;
        faithPoint=0;
        resourcesBuffer.clear();
    }

    //Metodi specifici azione 3 produzione mercato (player preme su azione 3 va al mercato mi ritorna un vettore da mettere nella warehouse)

    /**
     * Convert a list of resources to a list of concrete resources, remove ANY and FAITH
     * @param resourcesSent: the original list
     * @return the arrayList with all the concrete resources*/
    private ArrayList<Resource> fromResourceToConcreteResource(ArrayList<Resource> resourcesSent){
        faithPoint=0;
        anyResource=0;
        Resource resourceAny = ResourceFactory.createResource(ResourceType.ANY, 0);
        Resource resourceFaith = ResourceFactory.createResource(ResourceType.FAITH, 0);
        while(resourcesSent.contains(resourceAny)){
            anyResource+= resourcesSent.get(resourcesSent.indexOf(resourceAny)).getValue();
            resourcesSent.remove(resourcesSent.indexOf(resourceAny));
        }
        while(resourcesSent.contains(resourceFaith)){
            faithPoint+=resourcesSent.get(resourcesSent.indexOf(resourceFaith)).getValue();
            resourcesSent.remove(resourcesSent.indexOf(resourceFaith));
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
    public void addToWarehouse(boolean normalDepot, int index, Resource resource) throws TooMuchResourceDepotException, InvalidOrganizationWarehouseException, CantModifyDepotException, NegativeResourceException {
        if(normalDepot){
            currWarehouse.modifyStandardDepotValueAt(index, resource);
        }
        else{
            currWarehouse.modifyLeaderDepotValueAt(index, resource);
        }
    }

    //Metodi specifici azione 2 produzione carte dev (player preme su azione 2 va al cardMNG e per ogni carta che preme la aggiunge alle carte da produrre
    //io controllo se la posso poi effettivamente comprare, se posso aggiungo il costo al mio buffer, quando submitta quelle produzioni tolgo effettivamente le risorse da dove mi dice il
    //player e mi ritorna il vettore di risorse prodotte

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
        strongbox.changeResourceValueOf(resource);
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
            currWarehouse.modifyStandardDepotValueAt(index, ResourceFactory.createResource(resource.getType(), -resource.getValue()));
        }
        else{
            currWarehouse.modifyLeaderDepotValueAt(index, ResourceFactory.createResource(resource.getType(), -resource.getValue()));
        }
    }

    /**
     * Subtract to the strongbox the resource
     * @param resource i want to subtract */
    public void subtractToStrongbox(Resource resource){
        strongbox.changeResourceValueOf(ResourceFactory.createResource(resource.getType(), -resource.getValue()));
    }

    /**
     * Convert one ANY to a specific resource
     * @param type of resource i want to have
     * @return the resource i'm asking for*/
    private Resource changeAnyInResource(ResourceType type) throws NoMoreAnyResourceException{
        if(anyResource<=0){
            throw new NoMoreAnyResourceException("No more 'any' to convert");
        }
        return ResourceFactory.createResource(type, 1);
    }

    /**
     * Used to add a resource value or the resource itself in the buffer
     * @param resource I want to add from the buffer
     * @throws NegativeResourceException if resource'll go under value 0*/
    public void addToBuffer(Resource resource) throws NegativeResourceException {
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
            resourcesBuffer.get(resourcesBuffer.indexOf(resource)).addValue(- resource.getValue());
        }
    }

    /**
     * Used to add a resource value or the resource itself in the resource to produce
     * @param resource I want to add
     * @throws NegativeResourceException if resource'll go under value 0*/
    public void addToResourcesToProduce(Resource resource) throws NegativeResourceException {
        if(resourcesToProduce.contains(resource)){
            resourcesToProduce.get(resourcesToProduce.indexOf(resource)).addValue(resource.getValue());
        }
        else{
            resourcesToProduce.add(resource);
        }
    }

    /**
     * Compute if u can afford some resources
     * @param resources i would like to be able to afford
     * @return true if u can false if u can't*/
    public boolean canIAfford(ArrayList<Resource> resources, boolean checkDiscount) throws NegativeResourceException {
        int sum;
        for(Resource res : resources){
            if(resourcesBuffer.contains(res)){
                sum=-resourcesBuffer.get(resourcesBuffer.indexOf(res)).getValue();
            }
            else{
                sum=0;
            }
            sum+= currWarehouse.howManyDoIHave(res.getType());
            sum+= strongbox.howManyDoIHave(res.getType());
            if(discounts.contains(res) && checkDiscount){
                try{
                    res.addValue(-discounts.get(discounts.indexOf(res)).getValue());
                }catch(NegativeResourceException e){
                    res.addValue(-res.getValue());
                }
            }
            if(sum<=(res.getValue()) && res.getType() != ResourceType.ANY)
                return false;
        }

        for(Resource res : resources){
            addToBuffer(res);
        }
        return true;
    }

   /**
    * switch the resource from fromDepot and toDepot
    * @param fromDepot the first depot
    * @param toDepot the second depot
    * */
   public void switchResourceFromDepotToDepot(int fromDepot, int toDepot) throws CantModifyDepotException, TooMuchResourceDepotException, InvalidOrganizationWarehouseException {
       supportResource = currWarehouse.removeResourceAt(fromDepot);
       try{
           currWarehouse.setResourceDepotAt(toDepot, supportResource);
       }
       catch(CantModifyDepotException | TooMuchResourceDepotException | InvalidOrganizationWarehouseException e){
           currWarehouse.setResourceDepotAt(fromDepot, supportResource);
           throw e;
       }
   }

    /**
     * Discard resource
     * @param resource i want to discard*/
    public void discardResource(Resource resource){
        //TODO: observer notify how many cell all the other players can move in the fait
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
    public void addDiscount(Resource resource) throws NegativeResourceException {
        if(discounts.contains(resource)){
            discounts.get(discounts.indexOf(resource)).addValue(resource.getValue());
        }
        else{
            discounts.add(resource);
        }
    }

    //metodo per me
    public void print(){
        System.out.println("ANY: "+anyResource+"-"+" FAITH: "+faithPoint);
        currWarehouse.print();
        strongbox.print();
    }

}
