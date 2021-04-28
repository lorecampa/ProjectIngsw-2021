package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.exception.JsonFileConfigError;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Leader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class MastersOfRenaissanceTest
{
    private  ObjectMapper mapper;
    @BeforeEach
    void init(){
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }
    @Test
    public void CardPrintTest() throws IOException, JsonFileConfigError {

        ArrayList<Leader> leaders = mapper
                .readValue(new File("src/main/resources/json/leader.json"), new TypeReference<ArrayList<Leader>>() {});

        for(Leader leader: leaders){
            System.out.println(leader);
        }
        assertEquals(16, leaders.size());


        Development[] developmentsJson = mapper
                .readValue(new File("src/main/resources/json/development.json"), Development[].class);
        int deckSize = developmentsJson.length;
        if (deckSize % (3 * 4) != 0) {
            throw new JsonFileConfigError("Deck Development not valid");
        }
        final int DEPTH_DECK = deckSize / (3 * 4);

        ArrayList<ArrayList<ArrayList<Development>>> deckDevelopment = new ArrayList<>();
        int cardIndex = 0;
        for (int i = 0; i < 3; i++){
            //row
            ArrayList<ArrayList<Development>>  row = new ArrayList<>();
            for (int j = 0; j < 4; j++){
                //depth
                ArrayList<Development> cardBlock = new ArrayList<>();
                for (int k = 0; k < DEPTH_DECK; k++) {
                    cardBlock.add(developmentsJson[cardIndex]);
                    cardIndex++;
                }
                Collections.shuffle(cardBlock);
                row.add(cardBlock);
            }
            deckDevelopment.add(row);
        }

        for(Development dev: developmentsJson){
            System.out.println(dev);
        }
        assertEquals(48, deckSize);

    }
}
