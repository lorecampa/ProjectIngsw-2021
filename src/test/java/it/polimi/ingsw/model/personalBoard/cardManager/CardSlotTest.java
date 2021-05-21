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
        devLv1Pv1 = new Development(1, req ,null, null,1, Color.BLUE);
        devLv2Pv1 = new Development(2, req ,null, null,2, Color.YELLOW);
        devLv3Pv1 = new Development(3, req ,null, null,3, Color.GREEN);
    }

    @Test
    void getLvReached() {
        assertEquals(0, cs.getLvReached());

        assertDoesNotThrow(() -> cs.insertCard(devLv1Pv1));
        cs.emptyBuffer();
        assertEquals(1, cs.getLvReached());

        assertDoesNotThrow(() -> cs.insertCard(devLv2Pv1));
        cs.emptyBuffer();
        assertEquals(2, cs.getLvReached());

        assertDoesNotThrow(()-> cs.insertCard(devLv3Pv1));
        cs.emptyBuffer();
        assertEquals(3, cs.getLvReached());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void insertCard(int index) {
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
    void victoryPoints(){
        assertDoesNotThrow(()->cs.insertCard(devLv1Pv1));
        cs.emptyBuffer();
        assertDoesNotThrow(()->cs.insertCard(devLv2Pv1));
        cs.emptyBuffer();
        assertEquals(3, cs.getVictoryPoint());
        assertDoesNotThrow(()->cs.insertCard(devLv3Pv1));
        cs.emptyBuffer();
        assertEquals(6, cs.getVictoryPoint());
    }


}