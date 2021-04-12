package it.polimi.ingsw.model.card.Effect.Creation;

import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.card.Effect.State;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DiscountEffectTest {
    ResourceManager rm;
    ArrayList<Resource> discounts = new ArrayList<>();
    Effect effect;

    @BeforeEach
    void init(){
        rm = new ResourceManager();
        rm.newTurn();
        Resource res1 = ResourceFactory.createResource(ResourceType.COIN, 2);
        Resource res2 = ResourceFactory.createResource(ResourceType.SHIELD, 3);
        Resource res3 = ResourceFactory.createResource(ResourceType.SERVANT, 1);
        discounts.add(res1);
        discounts.add(res2);
        discounts.add(res3);

    }
    @Test
    void doEffect() {
        effect = new DiscountEffect(discounts);
        effect.attachResourceManager(rm);
        assertDoesNotThrow(()->effect.doEffect(State.CREATION_STATE));

        //create a requirementCost of a development card
        ArrayList<Resource> costBuyDevelopment = new ArrayList<>();
        Resource res1 = ResourceFactory.createResource(ResourceType.COIN, 2);
        Resource res2 = ResourceFactory.createResource(ResourceType.SHIELD, 3);
        Resource res3 = ResourceFactory.createResource(ResourceType.SERVANT, 1);
        costBuyDevelopment.add(res1);
        costBuyDevelopment.add(res2);
        costBuyDevelopment.add(res3);

        //modify canIAfford
        //assertTrue(rm.canIAfford(costBuyDevelopment, true));

    }
}