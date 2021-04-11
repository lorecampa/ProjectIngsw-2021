package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Leader;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MastersOfRenaissanceTest
{

    @Test
    public void CardPrintTest() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);


        ArrayList<Leader> leaders =
                mapper.readValue(new File("src/main/resources/json/leader.json"), new TypeReference<ArrayList<Leader>>() {});

        for(Leader leader: leaders){
            System.out.println(leader);
        }
        assertEquals(16, leaders.size());


        ArrayList<Development> developmentsJson =
                mapper.readValue(new File("src/main/resources/json/development.json"), new TypeReference<ArrayList<Development>>() {});

        for(Development dev: developmentsJson){
            System.out.println(dev);
        }
        assertEquals(48, developmentsJson.size());

    }
}
