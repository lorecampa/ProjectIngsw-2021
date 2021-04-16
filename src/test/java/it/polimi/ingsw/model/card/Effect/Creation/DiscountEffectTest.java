package it.polimi.ingsw.model.card.Effect.Creation;

import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.card.Effect.State;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DiscountEffectTest {
    ResourceManager rm = new ResourceManager();
    ArrayList<Resource> discounts;
    Effect effect;
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

    @BeforeEach
    void init(){
        rm.newTurn();
        //creates a discounts
        discounts = resourceArray(2,1,0,0,0,0);
        effect = new DiscountEffect(discounts);
        effect.attachResourceManager(rm);
        assertDoesNotThrow(()->effect.doEffect(State.CREATION_STATE));
    }


    @ParameterizedTest
    @ValueSource(ints = {0,1,2, 3})
    void discountWithNoResources(int index) {
        ArrayList<Resource> costBuyDevelopment;
        switch (index){
            case 0:
                //create a requirementCost of a development card
                costBuyDevelopment = resourceArray(1,0,0,0,0,1);
                //modify canIAfford
                assertTrue(rm.canIAfford(costBuyDevelopment, true));
                break;
            case 1:
                costBuyDevelopment = resourceArray(2,0,0,0,0,2);
                assertFalse(rm.canIAfford(costBuyDevelopment, true));
                break;
            case 2:
                costBuyDevelopment = resourceArray(2,1,0,0,0,0);
                assertTrue(rm.canIAfford(costBuyDevelopment, true));
                break;
            case 3:
                costBuyDevelopment = new ArrayList<>();
                assertTrue(rm.canIAfford(costBuyDevelopment, true));
        }

    }



}