package it.polimi.ingsw.model.card.requirement;


import it.polimi.ingsw.exception.CardWithHigherOrSameLevelAlreadyIn;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.util.JacksonMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class CardReqTest {

    static CardManager cm;
    @BeforeAll
    static void  init() throws IOException, CardWithHigherOrSameLevelAlreadyIn {
        cm = new CardManager();
        Development[] developmentsJson = JacksonMapper.getInstance()
                .readValue(new File("src/main/resources/json/development.json"), Development[].class);

        //slot 1 (1: purple level 1 -  2: yellow level 2)
        //slot 2 (1: blue   level 1 -  2: blue   level 2)
        //slot 3 (1: yellow level 1 -  2: yellow level 2 - 3: green level 3)

        cm.addDevelopmentCardTo(developmentsJson[4], 0);
        cm.addDevelopmentCardTo(developmentsJson[30], 0);

        cm.addDevelopmentCardTo(developmentsJson[10], 1);
        cm.addDevelopmentCardTo(developmentsJson[24], 1);

        cm.addDevelopmentCardTo(developmentsJson[15], 2);
        cm.addDevelopmentCardTo(developmentsJson[31], 2);
        cm.addDevelopmentCardTo(developmentsJson[33], 2);


    }

    @Test
    void NormalTest1() {
        //slot 1 (1: purple level 1 -  2: yellow level 2)
        //slot 2 (1: blue   level 1 -  2: blue   level 2)
        //slot 3 (1: yellow level 1 -  2: yellow level 2 - 3: green level 3)

        Requirement req1 = new CardReq(Color.BLUE, 1, 2);
        Requirement req2 = new CardReq(Color.YELLOW, 2, 2);
        Requirement req3 = new CardReq(Color.PURPLE, 1, 2);


        req1.attachCardManager(cm);
        req2.attachCardManager(cm);
        req3.attachCardManager(cm);


        assertFalse(req1.checkRequirement(false));
        assertTrue(req2.checkRequirement(false));
        assertFalse(req3.checkRequirement(false));

    }

    @Test
    void NormalTest2() {
        //slot 1 (1: purple level 1 -  2: yellow level 2)
        //slot 2 (1: blue   level 1 -  2: blue   level 2)
        //slot 3 (1: yellow level 1 -  2: yellow level 2 - 3: green level 3)
        Requirement req1 = new CardReq(Color.GREEN, 3, 1);
        Requirement req2 = new CardReq(Color.BLUE, 3, 1);

        req1.attachCardManager(cm);
        req2.attachCardManager(cm);

        assertTrue(req1.checkRequirement(false));
        assertFalse(req2.checkRequirement(false));

    }
}