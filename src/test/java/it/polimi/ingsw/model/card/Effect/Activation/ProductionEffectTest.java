package it.polimi.ingsw.model.card.Effect.Activation;

import it.polimi.ingsw.exception.NotEnoughRequirementException;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.TurnState;
import it.polimi.ingsw.model.personalBoard.resourceManager.Depot;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductionEffectTest {

    static ResourceManager rm = new ResourceManager();
    private ArrayList<Resource> resourceArray(int coin, int shield, int servant, int stone, int faith, int any){
        ArrayList<Resource> production = new ArrayList<>();
        if (coin > 0){
            production.add(ResourceFactory.createResource(ResourceType.COIN, coin));
        }
        if (shield > 0){
            production.add(ResourceFactory.createResource(ResourceType.SHIELD, shield));
        }
        if (servant > 0){
            production.add(ResourceFactory.createResource(ResourceType.SERVANT, servant));
        }
        if (stone > 0){
            production.add(ResourceFactory.createResource(ResourceType.STONE, stone));
        }
        if (faith > 0){
            production.add(ResourceFactory.createResource(ResourceType.FAITH, faith));
        }
        if (any > 0){
            production.add(ResourceFactory.createResource(ResourceType.ANY, any));
        }
        return production;
    }


    @BeforeAll
     static void init(){
        Resource res1 = ResourceFactory.createResource(ResourceType.SHIELD, 1);
        Resource res2 = ResourceFactory.createResource(ResourceType.COIN, 6);
        Resource res3 = ResourceFactory.createResource(ResourceType.STONE, 2);
        Resource res4 = ResourceFactory.createResource(ResourceType.SERVANT, 2);
        //strongbox (Coin 6) (Shield 1) (Servant 2) (Stone 2)
        //warehouse (0 -> ) (1 -> ) (2 -> 2 servant)
        rm.addToStrongbox(res1);
        rm.addToStrongbox(res2);
        rm.addToStrongbox(res3);
        assertDoesNotThrow(()->rm.addToWarehouse(true, 2, res4));


    }

    @Test
    @Order(1)
    void normalProduction1() {
        rm.restoreRM();
        //now those are the resources
        //strongbox (Coin 6) (Shield 1) (Servant 2) (Stone 2)
        //warehouse (0 -> ) (1 -> ) (2 -> 2 servant)
        ArrayList<Resource> productionCost = resourceArray(0, 1, 0, 2, 0, 1);
        ArrayList<Resource> productionAcquired = resourceArray(2, 0, 2, 0, 0, 0);


        Effect effect = new ProductionEffect(productionCost, productionAcquired);
        effect.attachResourceManager(rm);

        assertDoesNotThrow(()->effect.doEffect(TurnState.PRODUCTION_ACTION));
        assertEquals(1, rm.getAnyRequired());

        //subtract production cost
        //1 any -> 1 servant
        assertDoesNotThrow(()->rm.subToWarehouse(true, 2, ResourceFactory.createResource(ResourceType.SERVANT, 1)));

        //normal resources
        assertDoesNotThrow(()->rm.subToStrongbox(productionCost.get(0)));
        assertDoesNotThrow(()->rm.subToStrongbox(productionCost.get(1)));

        //make production
        rm.doProduction();
    }

    @Test
    @Order(2)
    void normalProduction2() {
        rm.restoreRM();
        //now those are the resources
        //strongbox (Coin 8) (Shield 0) (Servant 2) (Stone 0)
        //warehouse (0 -> ) (1 -> ) (2 -> 1 servant)
        ArrayList<Resource> productionCost = resourceArray(8, 0, 1, 0, 0, 2);
        ArrayList<Resource> productionAcquired = resourceArray(2, 5, 0, 10, 0, 0);

        Effect effect = new ProductionEffect(productionCost, productionAcquired);
        effect.attachResourceManager(rm);

        assertDoesNotThrow(()->effect.doEffect(TurnState.PRODUCTION_ACTION));
        assertEquals(2, rm.getAnyRequired());

        //subtract production cost
        //2 any -> 2 servant (one from warehouse and one from strongbox)
        assertDoesNotThrow(()->rm.subToWarehouse(true, 2, ResourceFactory.createResource(ResourceType.SERVANT, 1)));
        assertDoesNotThrow(()->rm.subToStrongbox(ResourceFactory.createResource(ResourceType.SERVANT, 1)));

        //normal resources
        assertDoesNotThrow(()->rm.subToStrongbox(productionCost.get(0)));
        assertDoesNotThrow(()->rm.subToStrongbox(productionCost.get(1)));


        rm.doProduction();
    }

    @Test
    @Order(3)
    void notEnoughResources() {
        rm.restoreRM();
        //now those are the resources
        //strongbox (Coin 2) (Shield 5) (Servant 0) (Stone 10)
        //warehouse (0 -> ) (1 -> ) (2 -> )
        ArrayList<Resource> productionCost = resourceArray(2, 5, 0, 9, 0, 2);
        ArrayList<Resource> productionAcquired = resourceArray(0, 0, 1, 0, 0, 0);

        Effect effect = new ProductionEffect(productionCost, productionAcquired);
        effect.attachResourceManager(rm);

        assertThrows(NotEnoughRequirementException.class, ()-> effect.doEffect(TurnState.PRODUCTION_ACTION));

    }

    @Test
    @Order(4)
    void notResourceEffect() {
        rm.restoreRM();
        //now those are the resources
        //strongbox (Coin 2) (Shield 5) (Servant 0) (Stone 10)
        //warehouse (0 -> ) (1 -> ) (2 -> )
        ArrayList<Resource> productionCost = new ArrayList<>();
        ArrayList<Resource> productionAcquired = new ArrayList<>();

        Effect effect = new ProductionEffect(productionCost, productionAcquired);
        effect.attachResourceManager(rm);

        assertDoesNotThrow(()->effect.doEffect(TurnState.PRODUCTION_ACTION));

        //create a leader depot with 2 coins
        ArrayList<Depot> depots=new ArrayList<>();
        depots.add(new Depot(ResourceFactory.createResource(ResourceType.COIN, 0), 2));
        rm.addLeaderDepot(depots);
        assertDoesNotThrow(()->rm.addToWarehouse(false, 0, ResourceFactory.createResource(ResourceType.COIN, 2)));
    }

    @Test
    @Order(5)
    void normalProduction3() {
        rm.restoreRM();
        //now those are the resources
        //strongbox (Coin 2) (Shield 5) (Servant 0) (Stone 10)
        //warehouse (0 -> ) (1 -> ) (2 -> )
        //leaderDepot (2 Coin)
        ArrayList<Resource> productionCost = resourceArray(2, 5, 0, 10, 0, 2);
        ArrayList<Resource> productionAcquired = resourceArray(0, 0, 1, 0, 0, 0);

        Effect effect = new ProductionEffect(productionCost, productionAcquired);
        effect.attachResourceManager(rm);

        assertDoesNotThrow(()->effect.doEffect(TurnState.PRODUCTION_ACTION));

    }

}