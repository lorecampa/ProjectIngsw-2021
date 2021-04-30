package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.exception.JsonFileModificationError;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.token.Token;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;


/**
 * Class GameSetting defines a class that represent the game data, it first read all custom game
 * components from json files and then let the player customize these classes
 */
public class GameSetting {
    private final ObjectMapper mapper;
    private final int numberOfPlayer;
    private  Development baseProduction;
    private ArrayList<ArrayList<ArrayList<Development>>> deckDevelopment;
    private LinkedList<Leader> deckLeader;
    private FaithTrack faithTrack;
    private Market market;
    private LinkedList<Token> deckToken = new LinkedList<>();
    private final int leaderAtStart = 4;


    /**
     * Constructor GameSetting creates a new game setting instance. It first creates all the
     * default class from json files meanwhile checking their authenticity and integrity
     * @param numberOfPlayers of type int - the number of player of the game
     * @throws IOException
     * @throws JsonFileModificationError
     */
    public GameSetting(int numberOfPlayers) throws IOException, JsonFileModificationError {
        //setting up mapper
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        createDefaultBaseProduction();
        createDefaultFaithTrack();
        createDefaultMarket();
        createDefaultDeckDevelopment();
        createDefaultDeckLeader();

        this.numberOfPlayer = numberOfPlayers;
        if (numberOfPlayers == 1)
            createDeckToken();
    }



    private void createDefaultFaithTrack() throws IOException {
        faithTrack = mapper.readValue(new File("src/main/resources/json/FaithTrack.json"), FaithTrack.class);
    }

    private void createDefaultBaseProduction() throws IOException {
        baseProduction = mapper.readValue(new File("src/main/resources/json/baseProduction.json"), Development.class);
    }

    private void createDefaultMarket() throws IOException {
        market = mapper.readValue(new File("src/main/resources/json/market.json"), Market.class);
    }

    private void createDeckToken() throws IOException, JsonFileModificationError {
        final int INITIAL_SIZE_TOKEN_DECK = 6;

        deckToken = mapper.readValue(new File("src/main/resources/json/token.json"),
                new TypeReference<LinkedList<Token>>() {});
        if (deckToken.size() != INITIAL_SIZE_TOKEN_DECK){
            throw new JsonFileModificationError("Deck token size wrong");
        }
        Collections.shuffle(this.deckToken);

    }

    private void createDefaultDeckLeader() throws IOException, JsonFileModificationError {
        final int INITIAL_SIZE_LEADER_DECK = 16;

        deckLeader = mapper
                .readValue(new File("src/main/resources/json/leader.json"),
                        new TypeReference<LinkedList<Leader>>() {});
        if (deckLeader.size() != INITIAL_SIZE_LEADER_DECK){
            throw new JsonFileModificationError("Leader deck size wrong");
        }
        Collections.shuffle(deckLeader);
    }

    private void createDefaultDeckDevelopment() throws IOException, JsonFileModificationError {
        final int INITIAL_ROW_DECK = 3;
        final int INITIAL_COLUMN_DECK = 4;
        final int INITIAL_DEPTH_DECK = 4;
        final int INITIAL_SIZE_DECK = INITIAL_ROW_DECK * INITIAL_COLUMN_DECK * INITIAL_DEPTH_DECK;

        Development[] developmentsJson = mapper
                .readValue(new File("src/main/resources/json/development.json"), Development[].class);

        if (developmentsJson.length != INITIAL_SIZE_DECK) {
            throw new JsonFileModificationError("Deck Development size wrong");
        }

        deckDevelopment = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < INITIAL_ROW_DECK; i++){
            //row
            ArrayList<ArrayList<Development>> row = new ArrayList<>();
            for (int j = 0; j < INITIAL_COLUMN_DECK; j++){
                //depth
                ArrayList<Development> cardBlock = new ArrayList<>();
                for (int k = 0; k < INITIAL_DEPTH_DECK; k++) {
                    cardBlock.add(developmentsJson[index]);
                    index++;
                }
                Collections.shuffle(cardBlock);
                row.add(cardBlock);
            }
            deckDevelopment.add(row);
        }
    }


    public Development getBaseProduction() {
        return baseProduction;
    }
    public ArrayList<ArrayList<ArrayList<Development>>> getDeckDevelopment() {
        return deckDevelopment;
    }
    public LinkedList<Leader> getDeckLeader() {
        return deckLeader;
    }
    public FaithTrack getFaithTrack() {
        return faithTrack;
    }
    public Market getMarket() {
        return market;
    }
    public LinkedList<Token> getDeckToken() {
        return deckToken;
    }
    public int getLeaderAtStart(){
        return leaderAtStart;
    }
    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }
}
