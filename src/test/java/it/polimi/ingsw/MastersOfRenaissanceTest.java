package it.polimi.ingsw;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.exception.JsonFileModificationError;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.GameSetting;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class MastersOfRenaissanceTest
{
    private GameMaster gm;
    private GameSetting gs;
    private final String MATCH_SAVING_PATH = "MatchSaving";

    @BeforeEach
    void init() throws IOException, URISyntaxException, JsonFileModificationError {
        gs = new GameSetting(3);
        ArrayList<String> players = new ArrayList<>();
        players.add("Lorenzo");
        players.add("Matteo");
        players.add("Davide");
        gm = new GameMaster(gs, players);



    }




    @Test
    void prova(){


        ObjectMapper mapper = new ObjectMapper();

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);


        try {
            FileWriter file = new FileWriter(Server.MATCH_SAVING_PATH + "/10.txt");
            file.write(mapper.writeValueAsString(gm));
            file.close();
        } catch (IOException e) {
            System.out.println("Error serialization");
            e.printStackTrace();
        }

        File fileToRead = new File(Server.MATCH_SAVING_PATH + "/10.txt");
        try {
            GameMaster gm = mapper.readValue(fileToRead, GameMaster.class);
            System.out.println(gm.getNumberOfPlayer());
        } catch (IOException e) {
            System.out.println("Error deserialization");
            e.printStackTrace();
        }


    }
}
