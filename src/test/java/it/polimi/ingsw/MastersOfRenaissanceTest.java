package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.core.type.TypeReference;
import it.polimi.ingsw.exception.JsonFileConfigError;
import it.polimi.ingsw.message.ClientMessageHandler;
import it.polimi.ingsw.message.Handler;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.clientMessage.ActivateProductionMessage;
import it.polimi.ingsw.message.clientMessage.BuyDevelopmentCardMessage;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.util.JacksonMapper;
import it.polimi.ingsw.util.JacksonSerializer;
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
        JacksonSerializer<ClientMessageHandler> jacksonSerializer = new JacksonSerializer<>();
        Message<ClientMessageHandler> buyDevMessage = new BuyDevelopmentCardMessage(1,2);
        Message<ClientMessageHandler> actProd = new ActivateProductionMessage(5,1);
        Message<ClientMessageHandler> errorMess = new ErrorMessage("Errore 404");



        String buyDevSerialized = jacksonSerializer.serializeMessage(buyDevMessage);
        System.out.println(buyDevSerialized);

        String actProdSerialized = jacksonSerializer.serializeMessage(actProd);
        System.out.println(actProdSerialized);

        String errMessSerialized = jacksonSerializer.serializeMessage(errorMess);
        System.out.println(errMessSerialized);


        Message<ClientMessageHandler> messageDeserialized = jacksonSerializer.deserializeMessage(buyDevSerialized);
        ClientMessageHandler clientMessageHandler = new ClientMessageHandler();
        messageDeserialized.process(clientMessageHandler);


    }
}
