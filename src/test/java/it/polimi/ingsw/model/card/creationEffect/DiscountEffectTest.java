package it.polimi.ingsw.model.card.creationEffect;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DiscountEffectTest {
    OnCreationEffect onCreationEffect;
    ResourceManager resourceManager;
    ArrayList<Resource> discounts;
    @BeforeEach
    void init(){
        discounts = new ArrayList<>();
        resourceManager = new ResourceManager();
        discounts.add(ResourceFactory.createResource(ResourceType.COIN, 3));

    }



    @Test
    void discountsThrow() throws NegativeResourceException {
        discounts.add(ResourceFactory.createResource(ResourceType.COIN, -4));
        discounts.add(ResourceFactory.createResource(ResourceType.COIN, +3));
        onCreationEffect = new DiscountEffect(discounts);
        onCreationEffect.attachResourceManager(resourceManager);
        assertThrows(NegativeResourceException.class, ()->onCreationEffect.doCreationEffect());
        assertFalse(onCreationEffect.isUsed());


    }

    @Test
    void discountsNotTrows() {
        discounts.add(ResourceFactory.createResource(ResourceType.COIN, +3));
        discounts.add(ResourceFactory.createResource(ResourceType.STONE, +4));
        onCreationEffect = new DiscountEffect(discounts);
        onCreationEffect.attachResourceManager(resourceManager);
        assertDoesNotThrow(()->onCreationEffect.doCreationEffect());
        assertTrue(onCreationEffect.isUsed());
    }


}