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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
     * Construct a Game Setting that creates all the default class from json files
     * meanwhile checking their authenticity and integrity.
     * @param numberOfPlayers the number of player of the game.
     * @throws IOException if there's some error during the reading of a json file.
     * @throws JsonFileModificationError if a json file is not written correctly.
     */
    public GameSetting(int numberOfPlayers) throws IOException, JsonFileModificationError{
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

    /**
     * Creates the faith track from the json file.
     * @throws IOException if there's some error during the reading of a json file.
     */
    private void createDefaultFaithTrack() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/json/FaithTrack.json");
        faithTrack = mapper.readValue(inputStream, FaithTrack.class);
    }

    /**
     * Creates the base production from the json file.
     * @throws IOException if there's some error during the reading of a json file.
     */
    private void createDefaultBaseProduction() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/json/baseProduction.json");
        baseProduction = mapper.readValue(inputStream, Development.class);
    }

    /**
     * Creates the market from the json file.
     * @throws IOException if there's some error during the reading of a json file.
     */
    private void createDefaultMarket() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/json/market.json");
        market = mapper.readValue(inputStream, Market.class);
    }

    /**
     * Creates the token's deck from the json file.
     * @throws IOException if there's some error during the reading of a json file.
     * @throws JsonFileModificationError if a json file is not written correctly.
     */
    private void createDeckToken() throws IOException, JsonFileModificationError{
        final int INITIAL_SIZE_TOKEN_DECK = 7;
        InputStream inputStream = getClass().getResourceAsStream("/json/token.json");
        deckToken = mapper.readValue(inputStream,
                new TypeReference<>() {});
        if (deckToken.size() != INITIAL_SIZE_TOKEN_DECK){
            throw new JsonFileModificationError("Deck token size wrong");
        }
    }

    /**
     * Creates the leader's deck from the json file.
     * @throws IOException if there's some error during the reading of a json file.
     * @throws JsonFileModificationError if a json file is not written correctly.
     */
    private void createDefaultDeckLeader() throws IOException, JsonFileModificationError {
        final int INITIAL_SIZE_LEADER_DECK = 16;
        InputStream inputStream = getClass().getResourceAsStream("/json/leader.json");
        deckLeader = mapper
                .readValue(inputStream,
                        new TypeReference<>() {});

        if (deckLeader.size() != INITIAL_SIZE_LEADER_DECK){
            throw new JsonFileModificationError("Leader deck size wrong");
        }
    }

    /**
     * Creates the deck of the development cards from the json file.
     * @throws IOException if there's some error during the reading of a json file.
     * @throws JsonFileModificationError if a json file is not written correctly.
     */
    private void createDefaultDeckDevelopment() throws IOException, JsonFileModificationError {
        final int INITIAL_ROW_DECK = 3;
        final int INITIAL_COLUMN_DECK = 4;
        final int INITIAL_DEPTH_DECK = 4;
        final int INITIAL_SIZE_DECK = INITIAL_ROW_DECK * INITIAL_COLUMN_DECK * INITIAL_DEPTH_DECK;
        InputStream inputStream = getClass().getResourceAsStream("/json/development.json");
        Development[] developmentsJson = mapper
                .readValue(inputStream, Development[].class);

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
                row.add(cardBlock);
            }
            deckDevelopment.add(row);
        }
    }

    /**
     * Return the base production.
     * @return the base production.
     */
    public Development getBaseProduction() {
        return baseProduction;
    }

    /**
     * Return the deck of the development cards.
     * @return the deck of the development cards.
     */
    public ArrayList<ArrayList<ArrayList<Development>>> getDeckDevelopment() {
        return deckDevelopment;
    }

    /**
     * Return the leader's deck.
     * @return the leader's deck.
     */
    public LinkedList<Leader> getDeckLeader() {
        return deckLeader;
    }

    /**
     * Return the faith track.
     * @return the faith track.
     */
    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    /**
     * Return the market.
     * @return the market.
     */
    public Market getMarket() {
        return market;
    }

    /**
     * Return the deck of tokens.
     * @return the deck of tokens.
     */
    public LinkedList<Token> getDeckToken() {
        return deckToken;
    }

    /**
     * Return the number of leaders at the start of the game.
     * @return the number of leaders at the start of the game.
     */
    public int getLeaderAtStart(){
        return leaderAtStart;
    }

    /**
     * Return the number of players.
     * @return the number of players.
     */
    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }
}
