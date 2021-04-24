package it.polimi.ingsw.model;

import com.fasterxml.jackson.core.type.TypeReference;
import it.polimi.ingsw.observer.CardManagerObserver;
import it.polimi.ingsw.observer.FaithTrackObserver;
import it.polimi.ingsw.observer.ResourceManagerObserver;
import it.polimi.ingsw.exception.DeckDevelopmentCardException;
import it.polimi.ingsw.exception.JsonFileConfigError;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.token.LorenzoIlMagnifico;
import it.polimi.ingsw.model.token.Token;
import it.polimi.ingsw.util.JacksonMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * GameMaster class
 */
public class GameMaster implements ResourceManagerObserver, FaithTrackObserver, CardManagerObserver, LorenzoIlMagnifico {
    private final static String NAME_LORENZO = "LorenzoIlMagnifico";

    private String currentPlayer = null;
    private final int numberOfPlayer;
    private final NavigableMap<String, PersonalBoard> playersPersonalBoard = new TreeMap<>();
    private final ArrayList<ArrayList<ArrayList<Development>>> deckDevelopment = new ArrayList<>();
    private LinkedList<Leader> deckLeader;
    private Market market;
    private int vaticanReportReached = 0;
    private LinkedList<Token> deckToken = new LinkedList<>();
    private boolean gameEnded = false;

    private static final int SIZE_DECK = 48;
    private static final int ROW_DECK = 3;
    private static final int COLUMN_DECK = 4;
    private static final int DEPTH_DECK = SIZE_DECK / (ROW_DECK * COLUMN_DECK);
    private static final int SIZE_TOKEN_DECK = 6;
    private static final int LEADERS_AT_START = 4;





    /**
     * Constructor GameMaster creates a new game. It creates a new Market, deck of Leader and deck of Development card,
     * moreover if it is a single player game instantiate LorenzoIlMagnifico as a Player
     * @param usernameCreator of type String -  username of the player that creates the game
     * @param numberOfPlayer of type int - number of players in game
     * @throws IOException when there are problems opening Json files when loading game information
     */
    public GameMaster(String usernameCreator, int numberOfPlayer) throws IOException, JsonFileConfigError {
        this.numberOfPlayer = numberOfPlayer;
        addPlayer(usernameCreator);

        createMarket();
        createDeckLeader();
        createDeckDevelopment();

        //single player
        if(numberOfPlayer == 1){
            //set the first player
            setCurrentPlayer(usernameCreator);
            addPlayer(NAME_LORENZO);
            createDeckToken();
        }

    }

    private void createMarket() throws IOException {
        market = JacksonMapper.getInstance()
                .readValue(new File("src/main/resources/json/market.json"), Market.class);
    }

    private void createDeckToken() throws IOException, JsonFileConfigError {
        deckToken = JacksonMapper.getInstance().readValue(new File("src/main/resources/json/token.json"),
                new TypeReference<LinkedList<Token>>() {});
        if (deckToken.size() != SIZE_TOKEN_DECK){
            throw new JsonFileConfigError("Deck token size wrong");
        }
        Collections.shuffle(this.deckToken);

    }

    private void createDeckLeader() throws IOException {
        deckLeader = JacksonMapper.getInstance()
                .readValue(new File("src/main/resources/json/leader.json"),
                new TypeReference<LinkedList<Leader>>() {});
        Collections.shuffle(deckLeader);
    }

    private void createDeckDevelopment() throws IOException, JsonFileConfigError {
        Development[] developmentsJson = JacksonMapper.getInstance()
                .readValue(new File("src/main/resources/json/development.json"), Development[].class);

        if (developmentsJson.length != SIZE_DECK) {
            throw new JsonFileConfigError("Deck Development size is not valid");
        }

        int index = 0;
        for (int i = 0; i < ROW_DECK; i++){
            //row
            ArrayList<ArrayList<Development>> row = new ArrayList<>();
            for (int j = 0; j < COLUMN_DECK; j++){
                //depth
                ArrayList<Development> cardBlock = new ArrayList<>();
                for (int k = 0; k < DEPTH_DECK; k++) {
                    cardBlock.add(developmentsJson[index]);
                    index++;
                }
                Collections.shuffle(cardBlock);
                row.add(cardBlock);
            }
            deckDevelopment.add(row);
        }
    }

    //control integrity

    /**
     * Method nextPlayer change the current player in the game when a new turn starts
     */
    public void nextPlayer(){
        String nextPlayer = playersPersonalBoard.higherKey(currentPlayer);
        if(nextPlayer == null){
            this.currentPlayer = playersPersonalBoard.firstKey();
        }else{
            this.currentPlayer = nextPlayer;
        }
    }




    /**
     * Method getNumActivePlayers gives the number of players still active in game
     * @return the numbers of player still on game
     */
    public int getNumActivePlayers(){
        return playersPersonalBoard.size();
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }

    public Market getMarket() {
        return market;
    }

    /**
     * Method getCurrentPlayer
     * @return String - the identifier of the current player in this turn
     */
    public String getCurrentPlayer(){
        return this.currentPlayer;
    }

    /**
     * Method addPlayer add the player in the list of the active players and creates for him a new personal board
     * @param username of type String - new player identifier
     * @throws IOException when creating the personal board causes problem opening the Json files
     */
    public void addPlayer(String username) throws IOException {
        playersPersonalBoard.put(username, new PersonalBoard(username));
    }

    /**
     *Method setCurrentPlayer is a setter for the current player of the turn
     * @param player of type String - identifier of the current player we want to set
     */
    public void setCurrentPlayer(String player){
        currentPlayer = player;
    }

    /**
     * Method getPersonalBoard is a getter for the personal board associated with a certain username
     * @param username of type String - player identifier
     * @return PersonalBoard - personal board associated with username, null if username is not in game
     */
    public PersonalBoard getPlayerPersonalBoard(String username) {
        return playersPersonalBoard.get(username);
    }

    /**
     * Method deliverLeaderCards delivers all the initial four card to all the players in the game
     */
    public void deliverLeaderCards() throws JsonFileConfigError {
        if (LEADERS_AT_START * numberOfPlayer > deckLeader.size()){
            throw new JsonFileConfigError("Not enough leaders to deliver");
        }
        for (PersonalBoard personalBoard: playersPersonalBoard.values()){
            CardManager cardManager = personalBoard.getCardManager();
            for (int i = 0; i < LEADERS_AT_START; i++){
                cardManager.addLeader(deckLeader.poll());
            }
        }
    }

    /**
     * Method popDeckDevelopmentCard return the first development Development card in the block of card deleting it
     * @param row of type int - row of the matrix
     * @param column of type int - column of the matrix
     * @return Development - the first card in the block associated with this coordinates (row, col)
     * @throws DeckDevelopmentCardException if the block of cards selected is empty
     * @throws IndexOutOfBoundsException if the coordinates are out of the matrix
     */
    public Development popDeckDevelopmentCard(int row, int column) throws DeckDevelopmentCardException, IndexOutOfBoundsException {
        if(deckDevelopment.get(row).get(column).isEmpty()){
            throw new DeckDevelopmentCardException("No development card at selection (Row: "+row+" Column: "+column+")");
        }
        Development development = deckDevelopment.get(row).get(column).get(0);
        deckDevelopment.get(row).get(column).remove(0);
        return development;
    }

    /**
     * Method removeDeckDevelopmentCard does the same of podDeckDevelopmentCard but without returning
     * the development card
     * @param row of type int - row to select
     * @param column of type int - column to select
     * @throws DeckDevelopmentCardException if the block of cards selected is empty
     * @throws IndexOutOfBoundsException if the coordinates are out of the matrix
     * @see GameMaster#popDeckDevelopmentCard(int, int)
     */
    public void removeDeckDevelopmentCard(int row, int column) throws DeckDevelopmentCardException, IndexOutOfBoundsException{
        if(deckDevelopment.get(row).get(column).isEmpty()){
            throw new DeckDevelopmentCardException("No development card at selection (Row: " + row +
                    " Column: " + column + ")");
        }
        deckDevelopment.get(row).get(column).remove(0);
    }


    /**
     * Method pushDeckDevelopmentCard insert ,in the first position of the block of card
     * selected (row, col), the development card (development)
     * @param row of type int - the row to select
     * @param column of type int - the column to select
     * @param development of type Development - the Development card to insert
     * @throws DeckDevelopmentCardException when you are trying to insert the card in a block that's
     * already full (4 card)
     * @throws IndexOutOfBoundsException when you are not selecting a block of card inside the matrix
     */
    public void pushDeckDevelopmentCard(int row, int column, Development development) throws DeckDevelopmentCardException, IndexOutOfBoundsException {
        if (deckDevelopment.get(row).get(column).size() == DEPTH_DECK){
            throw new DeckDevelopmentCardException("Too much card at selection (Row: " + row +
                    "Column: " + column + ")");
        }
        deckDevelopment.get(row).get(column).add(0, development);
    }

    /**
     * Method drawToken draws the token from the deckToken on the top,
     * puts it back in the bottom and then  applies its effect
     */
    public void drawToken() {
        //forse devo restituire il token per farlo vedere a schermo? bho
        Token token = deckToken.poll();
        deckToken.offer(token);
        if(token != null){
            try{
                token.doActionToken(this);
            } catch (DeckDevelopmentCardException  e) {
                //its all okay, just there is no more to delete
                //with the cardToken effect from the deck of development cards
            }
        }
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public int getSizeDeckLeader() {
        return deckLeader.size();
    }

    public int getSizeDeckToken() {return (deckToken != null) ? deckToken.size() : 0;}

    public ArrayList<ArrayList<ArrayList<Development>>> getDeckDevelopment() {
        return deckDevelopment;
    }

    /**
     * Method of FaithTrackObserver that manage the activation of a popeSpace from a player Faith Track
     * @param idVR is the id of the popeSpace activated
     */
    @Override
    public void vaticanReportReached(int idVR) {
        if(vaticanReportReached == idVR){
            for (PersonalBoard personalBoard : playersPersonalBoard.values()){
                personalBoard.getFaithTrack().popeFavorActivated(idVR);
            }
            vaticanReportReached ++;
        }
    }

    /**
     * Method of ResourceManagerObserver that manage the advancement of player after the current player discarded resources
     * @param numResources is the number of move each player must do
     */
    @Override
    public void discardResources(int numResources) {
        for (int i = 0; i < numResources; i++) {
            for (PersonalBoard personalBoard : playersPersonalBoard.values()){
                if(!personalBoard.equals(playersPersonalBoard.get(currentPlayer))){
                    personalBoard.getFaithTrack().increasePlayerPosition();
                }
            }
            for (PersonalBoard personalBoard : playersPersonalBoard.values()){
                if(!personalBoard.equals(playersPersonalBoard.get(currentPlayer))){
                    personalBoard.getFaithTrack().doCurrentCellAction();
                }
            }
        }
    }

    /**
     * Method of CardManagerObserver that manage to increase the
     * current player faith position after a card leader discard
     */
    public void discardLeader() {
        playersPersonalBoard.get(currentPlayer).getFaithTrack().movePlayer(1);
    }


    /**
     * Method discardDevelopment discard a specific number of card (num) of a certain color (color)
     * in the development deck starting from the cards of level 1 to the cards of level 3
     * @param color of type Color - target color
     * @param num of type int - number of cards to delete
     * @throws DeckDevelopmentCardException if it fails on deleting all the card required
     */
    @Override
    public void discardDevelopment(Color color, int num) throws DeckDevelopmentCardException {
        int rowReached = 0;
        int colorColumn = color.getColumnDeckDevelopment();
        int numDiscarded = 0;

        while(numDiscarded < num){
            try{
                removeDeckDevelopmentCard(rowReached, colorColumn);
                numDiscarded++;
            }catch (DeckDevelopmentCardException e){
                rowReached++;
            }
            if (rowReached == ROW_DECK){
                throw new DeckDevelopmentCardException("You have removed " + numDiscarded +
                        color.getDisplayName() + "cards.\n" +
                        "There are left " + (num - numDiscarded) + " "
                        +color.getDisplayName()+" cards to discard");
            }
        }
    }

    /**
     * Method shuffleToken shuffle the deck of Token for the single player mode
     */
    @Override
    public void shuffleToken() {
        Collections.shuffle(this.deckToken);
    }

    /**
     * Method increaseFaithPosition increase the faith position of Lorenzo il Magnifico of a given number (pos)
     * @param pos of type int - position to increase
     */
    @Override
    public void increaseFaithPosition(int pos) {
        this.playersPersonalBoard.get(NAME_LORENZO).getFaithTrack().movePlayer(pos);
    }





}