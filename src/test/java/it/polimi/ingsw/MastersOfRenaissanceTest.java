package it.polimi.ingsw;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.exception.JsonFileModificationError;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.GameSetting;
import it.polimi.ingsw.server.MatchData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        if (!Files.isDirectory(Paths.get(MATCH_SAVING_PATH))) {
            try {
                Files.createDirectories(Paths.get(MATCH_SAVING_PATH));
            } catch (IOException e) {
                //error saving match data
                e.printStackTrace();
            }
        }

        String fileName = MATCH_SAVING_PATH +"/"+ 0 + ".txt";
        try {
            FileWriter file = new FileWriter(fileName);
            ObjectMapper mapper = new ObjectMapper();

            mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);


            //MatchData matchSave = new MatchData(this);
            file.write(mapper.writeValueAsString(gm));

            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
