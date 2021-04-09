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
import java.util.*;

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
        String x = "";
        String path = "Cioa";
        System.out.println(path + x + "bella");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        ArrayList<Leader> leaders =
                mapper.readValue(new File("src/main/resources/json/leader.json"), new TypeReference<ArrayList<Leader>>() {});

        for(Leader leader: leaders){
            System.out.println(leader);
        }


        Development[] developmentsJson =
                mapper.readValue(new File("src/main/resources/json/development.json"), Development[].class);

        ArrayList<ArrayList<ArrayList<Development>>> developments = new ArrayList<>();
        int m = 0;
        int e = 0;
        for (int i = 0; i < 3; i++){
            //row
            ArrayList<ArrayList<Development>>  row = new ArrayList<>();
            for (int j = 0; j < 4; j++){
                //depth
                ArrayList<Development> block = new ArrayList<>();
                for (int k = 0; k < 4; k++) {
                    block.add(developmentsJson[m]);
                    m += 4;
                    if (k == 3) {
                        e++;
                        m = e;
                    }
                }
                Collections.shuffle(block);
                row.add(block);
            }
            developments.add(row);
        }



        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 4; j++){
                for (int k = 0; k < 4; k++){
                    System.out.println(developments.get(i).get(j).get(k));

                }
            }
        }

        LinkedList<Token> tokensJson = mapper.readValue(new File("src/main/resources/json/token.json"),
                new TypeReference<LinkedList<Token>>() {});

        for (int i = 0; i < tokensJson.size(); i++){
            Token token = tokensJson.poll();
            System.out.println(token);
            tokensJson.offer(token);
        }






        Development baseProduction =
                mapper.readValue(new File("src/main/resources/json/baseProduction.json"), Development.class);

        System.out.println(baseProduction);






    }
}
