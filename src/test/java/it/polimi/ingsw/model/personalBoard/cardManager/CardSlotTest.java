package it.polimi.ingsw.model.personalBoard.cardManager;

import it.polimi.ingsw.exception.CardWithHigherOrSameLevelAlreadyIn;
import it.polimi.ingsw.exception.NegativeResourceException;
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

    CardSlot cs=new CardSlot();
    ArrayList<Resource> resources = ResourceFactory.createAllConcreteResource();
    Requirement req = new ResourceReq(resources);
    Development devLv1Pv1 = new Development(1, (ArrayList<Requirement>) req,null, 1, Color.BLUE);
    Development devLv2Pv1 = new Development(1, (ArrayList<Requirement>) req,null, 2, Color.BLUE);

    @BeforeEach
    void init() {

        //add d1 and d2 to cs

    }

    @Test
    void getLvReached() {
        assertEquals(2, cs.getLvReached());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void insertCard(int index) {
        //I can't test this methods without a constructor for developers to choose the lv
        switch(index){
            case 0:
                assertThrows(CardWithHigherOrSameLevelAlreadyIn.class, ()->cs.insertCard(devLv2Pv1));
                break;
            case 1:
                assertDoesNotThrow(()->cs.insertCard(devLv1Pv1));
                break;
            default:
        }

    }

    @Test
    void getCardOfLv() {
        Development devTestLv1 = new Development(1, (ArrayList<Requirement>) req,null, 1, Color.BLUE);
        assertDoesNotThrow(()->cs.insertCard(devTestLv1));
        assertEquals(devTestLv1, cs.getCardOfLv(1));
    }


    @Test
    void howManyCardWithColor(){
        Development devTestYellow = new Development(1, (ArrayList<Requirement>) req,null, 1, Color.YELLOW);
        assertDoesNotThrow(()->cs.insertCard(devTestYellow));
        assertEquals(Color.YELLOW, devTestYellow.getColor());
    }
}