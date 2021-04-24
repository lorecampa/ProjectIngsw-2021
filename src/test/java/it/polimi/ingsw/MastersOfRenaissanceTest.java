package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.core.type.TypeReference;
import it.polimi.ingsw.exception.JsonFileConfigError;
import it.polimi.ingsw.message.CommandMessage;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.util.GsonUtil;
import it.polimi.ingsw.util.JacksonMapper;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class MastersOfRenaissanceTest
{

    @Test
    public void CardPrintTest() throws IOException, JsonFileConfigError {

        ArrayList<Leader> leaders = JacksonMapper.getInstance()
                .readValue(new File("src/main/resources/json/leader.json"), new TypeReference<ArrayList<Leader>>() {});

        for(Leader leader: leaders){
            System.out.println(leader);
        }
        assertEquals(16, leaders.size());


        Development[] developmentsJson = JacksonMapper.getInstance()
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

    @Test
    public void testJackson() throws IOException {
        Message commandMessage = new CommandMessage(MessageType.MARKET,2,2);

        String prova = JacksonMapper.getInstance().writeValueAsString(commandMessage);
        System.out.println(prova);

        Message dev = JacksonMapper.getInstance().readValue(prova,Message.class);
        System.out.println(dev.print());
    }
}
