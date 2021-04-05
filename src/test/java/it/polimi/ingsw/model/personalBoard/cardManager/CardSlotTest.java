package it.polimi.ingsw.model.personalBoard.cardManager;

import it.polimi.ingsw.exception.CardWithHigherOrSameLevelAlreadyIn;
import it.polimi.ingsw.model.card.Development;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CardSlotTest {

    CardSlot cs=new CardSlot();


    @BeforeEach
    void init(){
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
                //assertThrows(CardWithHigherOrSameLevelAlreadyIn.class, ()->cs.insertCard(d1));
                break;
            case 1:
                //assertDoesNotThrow(()->cs.insertCard(//d1));
                break;
            default:
        }

    }

    @Test
    void getCardOfLv() {
        //create a developer
        //add that dev to the slot
        // assertEquals(thecard, cs.getCardOfLv(KNOWN));
    }


    @Test
    void howManyCardWithColor(){
        //create a developer with a spec color
        //add that dev to the slot
        // assertEquals(color, xxxxxxxx.getColor());
    }
}