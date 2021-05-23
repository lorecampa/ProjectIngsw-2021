package it.polimi.ingsw.model.card.Effect.Creation;

import it.polimi.ingsw.client.data.EffectData;
import it.polimi.ingsw.model.PlayerState;
import it.polimi.ingsw.exception.NotEnoughRequirementException;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DiscountEffectTest {
    ResourceManager rm = new ResourceManager();
    ArrayList<Resource> costBuyDevelopment;
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
        //creates a discounts
        discounts = resourceArray(3,2,0,0,0,0);
        effect = new DiscountEffect(discounts);
        effect.attachResourceManager(rm);
        assertDoesNotThrow(()->effect.doEffect(PlayerState.LEADER_MANAGE_BEFORE));

        ArrayList<Resource> resStrongbox = resourceArray(2, 0, 0,0,0,0);
        for (Resource res: resStrongbox){
            rm.addToStrongbox(res);
        }

        rm.restoreRM();
    }


    @Test
    void strangeTest(){
        //strongbox (2 coin, 0 shield, 0 stone, 0 servant)
        //discounts (3 coin, 2 shield, 0 stone, 0 servant)

        costBuyDevelopment = resourceArray(4,1,0,0,0,2);
        assertDoesNotThrow(()->rm.canIAfford(costBuyDevelopment, true));

    }


    @ParameterizedTest
    @ValueSource(ints = {0,1,2, 3})
    void discountWithNoResources(int index) {
        //strongbox (2 coin, 0 shield, 0 stone, 0 servant)
        //discounts (3 coin, 2 shield, 0 stone, 0 servant)
        switch (index){
            case 0:
                costBuyDevelopment = resourceArray(5,0,0,0,0,3);
                assertThrows(NotEnoughRequirementException.class, () ->rm.canIAfford(costBuyDevelopment, true));
                break;
            case 1:
                costBuyDevelopment = resourceArray(6,0,0,0,0,0);
                assertThrows(NotEnoughRequirementException.class, () ->rm.canIAfford(costBuyDevelopment, true));
                break;
            case 2:
                costBuyDevelopment = resourceArray(2,1,0,0,0,4);
                assertDoesNotThrow(()->rm.canIAfford(costBuyDevelopment, true));
                break;
            case 3:
                costBuyDevelopment = new ArrayList<>();
                assertDoesNotThrow(()->rm.canIAfford(costBuyDevelopment, true));
        }

    }

    @Test
    void toDataTest(){
        ArrayList<Resource> cost = resourceArray(2, 0, 0, 0, 0, 0);
        DiscountEffect myEffect= new DiscountEffect(cost);
        EffectData effectData= myEffect.toEffectData();

        assertEquals("Discount effect: ", effectData.getDescriptions());
    }

    @Test
    void discardEffect(){
        ArrayList<Resource> cost= resourceArray(0, 2,0,0,0,0);
        assertDoesNotThrow(()->rm.canIAfford(cost, true));
        effect.discardEffect();
        ArrayList<Resource> cost2= resourceArray(0, 2,1,0,0,0);
        assertThrows(NotEnoughRequirementException.class, ()->rm.canIAfford(cost2,true));
    }

}