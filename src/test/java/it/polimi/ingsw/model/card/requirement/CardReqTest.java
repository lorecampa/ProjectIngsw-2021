package it.polimi.ingsw.model.card.requirement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.exception.CardWithHigherOrSameLevelAlreadyIn;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardReqTest {

    CardManager cm;
    @BeforeEach
    void init() throws IOException, CardWithHigherOrSameLevelAlreadyIn {
        cm = new CardManager();
        ObjectMapper mapper = new ObjectMapper();

        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        Development[] developmentsJson =
                mapper.readValue(new File("src/main/resources/json/development.json"), Development[].class);

        //level 1 -> (1 Green, 1 Purple, 1 Blue)
        //level 2 -> (1 Purple, 1 Blue, 1 Yellow)
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
        Requirement req3 = new CardReq(Color.YELLOW, 1, 1);


        req1.attachCardManager(cm);
        req2.attachCardManager(cm);

        assertTrue(req1.checkRequirement(false));
        assertTrue(req1.checkRequirement(false));

    }

    @Test
    void FalseTest() {

        Requirement req1 = new CardReq(Color.GREEN, 2, 1);
        Requirement req2 = new CardReq(Color.YELLOW, 1, 1);

        req1.attachCardManager(cm);
        req2.attachCardManager(cm);

        assertFalse(req1.checkRequirement(false));
        assertTrue(req1.checkRequirement(false));

    }
}