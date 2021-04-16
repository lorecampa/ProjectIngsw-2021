package it.polimi.ingsw.model.card.requirement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.exception.CardWithHigherOrSameLevelAlreadyIn;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
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
        ObjectMapper mapper = new ObjectMapper();

        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        Development[] developmentsJson =
                mapper.readValue(new File("src/main/resources/json/development.json"), Development[].class);

        //slot 1 (1: green  level 1 -  2: purple level 2)
        //slot 2 (1: purple level 1 -  2: blue   level 2)
        //slot 3 (1: blue   level 1 -  2: yellow level 2)

        for (int i = 0; i < 3 ; i++){
            Development dev = developmentsJson[i];
            Development dev2 = developmentsJson[i+17];

            cm.addDevelopmentCardTo(dev, i);
            cm.addDevelopmentCardTo(dev2, i);

        }

    }

    @Test
    void NormalTest() {

        Requirement req1 = new CardReq(Color.BLUE, 1, 2);
        Requirement req2 = new CardReq(Color.YELLOW, 2, 1);
        Requirement req3 = new CardReq(Color.PURPLE, 1, 2);


        req1.attachCardManager(cm);
        req2.attachCardManager(cm);
        req3.attachCardManager(cm);


        assertTrue(req1.checkRequirement(false));
        assertTrue(req2.checkRequirement(false));
        assertTrue(req3.checkRequirement(false));


    }

    @Test
    void FalseTest() {

        Requirement req1 = new CardReq(Color.GREEN, 2, 1);
        Requirement req2 = new CardReq(Color.YELLOW, 2, 2);

        req1.attachCardManager(cm);
        req2.attachCardManager(cm);

        assertFalse(req1.checkRequirement(false));
        assertFalse(req2.checkRequirement(false));

    }
}