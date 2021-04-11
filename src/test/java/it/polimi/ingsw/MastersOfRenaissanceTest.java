package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.token.Token;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class MastersOfRenaissanceTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        /*
        ArrayList<Leader> leaders =
                mapper.readValue(new File("src/main/resources/json/leader.json"), new TypeReference<ArrayList<Leader>>() {});

        for(Leader leader: leaders){
            System.out.println(leader);
        }
        */


        Development[] developmentsJson =
                mapper.readValue(new File("src/main/resources/json/development.json"), Development[].class);

        for(Development dev: developmentsJson){
            System.out.println(dev);
        }






    }
}
