package it.polimi.ingsw.model.personalBoard.cardManager;

import it.polimi.ingsw.exception.CardWithHigherOrSameLevelAlreadyIn;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.requirement.Requirement;
import it.polimi.ingsw.model.card.requirement.ResourceReq;
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

class CardSlotTest {

    CardSlot cs;
    ArrayList<Resource> resources = ResourceFactory.createAllConcreteResource();
    ResourceReq resourceReq;
    ArrayList<Requirement> req ;
    Development devLv1Pv1;
    Development devLv2Pv1;
    Development devLv3Pv1;

    @BeforeEach
    void init() {
        cs = new CardSlot();
        resourceReq = new ResourceReq(resources);
        req = new ArrayList<>();
        req.add(resourceReq);
        devLv1Pv1 = new Development(1, req ,null, 1, Color.BLUE);
        devLv2Pv1 = new Development(1, req ,null, 2, Color.BLUE);
        devLv3Pv1 = new Development(1, req ,null, 3, Color.GREEN);
    }

    @Test
    void getLvReached() {
        assertEquals(0, cs.getLvReached());
        assertDoesNotThrow(() -> cs.insertCard(devLv1Pv1));
        assertEquals(1, cs.getLvReached());
        assertDoesNotThrow(() -> cs.insertCard(devLv2Pv1));
        assertEquals(2, cs.getLvReached());
        assertDoesNotThrow(()-> cs.insertCard(devLv3Pv1));
        assertEquals(3, cs.getLvReached());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void insertCard(int index) {
        //I can't test this methods without a constructor for developers to choose the lv
        switch(index){
            case 0:
                assertThrows(CardWithHigherOrSameLevelAlreadyIn.class, ()->cs.insertCard(devLv2Pv1));
                break;
            case 1:
                assertDoesNotThrow(()->cs.insertCard(devLv1Pv1));
                break;
            case 2:
                assertThrows(CardWithHigherOrSameLevelAlreadyIn.class, () -> cs.insertCard(devLv3Pv1));
                break;
            default:
        }

    }

    @Test
    void getCardOfLv() {
        assertDoesNotThrow(()->cs.insertCard(devLv1Pv1));
        assertEquals(devLv1Pv1, cs.getCardOfLv(1));
        assertThrows(IndexOutOfBoundsException.class, () -> cs.getCardOfLv(0));
        assertThrows(IndexOutOfBoundsException.class, () -> cs.getCardOfLv(2));
        assertThrows(IndexOutOfBoundsException.class, () -> cs.getCardOfLv(3));
    }


    @Test
    void howManyCardWithColor(){
        assertDoesNotThrow(() -> cs.insertCard(devLv1Pv1));
        assertDoesNotThrow(() -> cs.insertCard(devLv2Pv1));
        assertDoesNotThrow(()-> cs.insertCard(devLv3Pv1));

        assertEquals(0, cs.howManyCardWithColor(Color.PURPLE));
        assertEquals(1, cs.howManyCardWithColor(Color.GREEN));
        assertEquals(2, cs.howManyCardWithColor(Color.BLUE));
    }
}