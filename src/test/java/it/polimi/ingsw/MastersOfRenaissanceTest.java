package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.exception.JsonFileModificationError;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;
import it.polimi.ingsw.message.serverMessage.ServerMessage;
import it.polimi.ingsw.model.GameSetting;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Leader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;


public class MastersOfRenaissanceTest
{

    private GameSetting gs;
    private GameSetting gsSinglePlayer;
    @BeforeEach
    void init(){
        assertDoesNotThrow(()-> gs = new GameSetting(4));
        assertDoesNotThrow(()-> gsSinglePlayer = new GameSetting(1));

    }
    @Test
    public void CardPrintTest() throws IOException, JsonFileModificationError {

        LinkedList<Leader> deckLeader = gs.getDeckLeader();
        deckLeader.forEach(System.out::println);

        assertEquals(16, deckLeader.size());

        ArrayList<ArrayList<ArrayList<Development>>> deckDevelopment = gs.getDeckDevelopment();
        deckDevelopment.forEach(System.out::println);


        assertEquals(3, deckDevelopment.size());
        assertEquals(4, deckDevelopment.get(0).size());
        assertEquals(4, deckDevelopment.get(0).get(0).size());



    }
    ObjectMapper mapper;

    @Test
    void test() throws JsonProcessingException {
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        ServerMessage message1 = new ConnectionMessage(ConnectionType.CONNECT,"a");
        String serverSer = mapper.writeValueAsString(message1);
        System.out.println(serverSer);



    }

}
