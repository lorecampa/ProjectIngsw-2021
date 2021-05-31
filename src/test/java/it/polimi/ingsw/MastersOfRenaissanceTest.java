package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.JsonFileModificationError;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;
import it.polimi.ingsw.message.clientMessage.ErrorType;
import it.polimi.ingsw.message.serverMessage.*;
import it.polimi.ingsw.model.GameSetting;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.server.Server;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
    void test1(){
        it.polimi.ingsw.Test.main(null);
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
    void test() throws IOException {
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        InputStream inputStream = getClass().getResourceAsStream("/json/leader.json");
        ArrayList<Leader> leaders = mapper.readValue(inputStream, new TypeReference<>() {});
        leaders.forEach(System.out::println);


        inputStream = getClass().getResourceAsStream("/json/development.json");
        ArrayList<Development> developments = mapper.readValue(inputStream, new TypeReference<>() {});
        developments.forEach(System.out::println);







    }


    @Test
    void gameStartForSetUpServer(){
        Map<Integer, String> points= new HashMap<>();
        points.put(16, "Lorenzo");
        points.put(17, "Teo");
        points.put(2000, "bbbb");
        points.put(1, "aaaaa");


        //TreeMap<Integer, String> test= new TreeMap<>(points);

        TreeMap<Integer, String> reverseSortedMap = new TreeMap<>(Collections.reverseOrder());
        reverseSortedMap.putAll(points);


        Set<Map.Entry<Integer, String>> entries = reverseSortedMap.entrySet();
        for(Map.Entry<Integer, String> entry : entries){
            PrintAssistant.instance.printf(entry.getKey()+": "+entry.getValue());
        }

    }


}
