package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.client.data.CardDevData;
import it.polimi.ingsw.client.data.DeckDevData;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.observer.*;
import it.polimi.ingsw.exception.DeckDevelopmentCardException;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.token.LorenzoIlMagnifico;
import it.polimi.ingsw.model.token.Token;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * GameMaster class
 */
public class GameMaster implements GameMasterObserver,Observable<ModelObserver>, LorenzoIlMagnifico {

    List<ModelObserver> modelObserverList = new ArrayList<>();

    private TurnState turnState;
    ObjectMapper mapper;
    private final static String NAME_LORENZO = "LorenzoIlMagnifico";
    private String currentPlayer = null;
    private int numberOfPlayer;
    private final NavigableMap<String, PersonalBoard> playersPersonalBoard = new TreeMap<>();
    private final ArrayList<String> playersTurn = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<Development>>> deckDevelopment;
    private LinkedList<Leader> deckLeader;
    private Market market;
    private String faithTrackSerialized;
    private String baseProductionSerialized;
    private LinkedList<Token> deckToken;
    private int depthDeckDevelopment;
    private int vaticanReportReached = 0;
    private int leaderAtStart;
    private boolean gameEnded = false;


    /**
     * Constructor GameMaster creates a new game. It creates a new Market, deck of Leader and deck of Development card,
     * moreover if it is a single player game instantiate LorenzoIlMagnifico as a Player
     * @param gameSetting of type GameSetting - the class that represent the game customization made by
     *                    the player
     * @throws IOException when there are problems opening Json files when loading game information
     */
    public GameMaster(GameSetting gameSetting,
                      ArrayList<String> players) throws IOException{

        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        this.numberOfPlayer = players.size();

        //game loading
        loadGameSetting(gameSetting);

        //setting up  players
        Collections.shuffle(players);
        for (String player: players){
            addPlayer(player);
            playersTurn.add(player);
        }

        if (numberOfPlayer == 1){
            addPlayer(NAME_LORENZO);
            playersTurn.add(NAME_LORENZO);
        }
    }




    /**
     * Method loadGameSetting is responsible to load the game data from the gameSetting
     * @param gameSetting of type GameSetting - game data
     * @throws JsonProcessingException if some error occurs in serialization
     */
    private void loadGameSetting(GameSetting gameSetting) throws JsonProcessingException {
        baseProductionSerialized = mapper.writeValueAsString(gameSetting.getBaseProduction());
        faithTrackSerialized = mapper.writeValueAsString(gameSetting.getFaithTrack());

        deckDevelopment = gameSetting.getDeckDevelopment();
        depthDeckDevelopment = deckDevelopment.get(0).get(0).size();
        deckDevelopment.stream().flatMap(ArrayList::stream).forEach(Collections::shuffle);

        deckLeader = gameSetting.getDeckLeader();
        Collections.shuffle(deckLeader);

        market = gameSetting.getMarket();
        market.attachGameMasterObserver(this);

        deckToken = gameSetting.getDeckToken();
        Collections.shuffle(deckToken);

        leaderAtStart = gameSetting.getLeaderAtStart();
        numberOfPlayer = gameSetting.getNumberOfPlayer();

    }


    /**
     * Method nextPlayer change the current player in the game when a new turn starts
     */
    public void nextPlayer(){
        if (currentPlayer == null || numberOfPlayer == 1) {
            this.currentPlayer = playersTurn.get(0);
        }else{
            int indexOfCurr=playersTurn.indexOf(currentPlayer);
            if(indexOfCurr==numberOfPlayer-1){
                currentPlayer=playersTurn.get(0);
            }
            else{
                currentPlayer=playersTurn.get(indexOfCurr+1);
            }
        }

        onTurnStateChange(TurnState.LEADER_MANAGE_BEFORE);
        notifyAllObservers(x -> x.currentPlayerChange(currentPlayer));
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
        FaithTrack playerFaithTrack = mapper.readValue(faithTrackSerialized, FaithTrack.class);
        playerFaithTrack.attachGameMasterObserver(this);

        ResourceManager playerResourceManager = new ResourceManager();
        playerResourceManager.attachGameMasterObserver(this);

        Development playerBaseProduction = mapper.readValue(baseProductionSerialized, Development.class);
        playerBaseProduction.setResourceManager(playerResourceManager);

        CardManager playerCardManager = new CardManager(playerBaseProduction);
        playerCardManager.attachGameMasterObserver(this);

        PersonalBoard playerPersonalBoard = new PersonalBoard(username, playerFaithTrack,
                playerResourceManager, playerCardManager);

        if (playersPersonalBoard.isEmpty())
            playerPersonalBoard.setInkwell(true);

        playersPersonalBoard.put(username, playerPersonalBoard);
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
    public void deliverLeaderCards() {
        Optional<Leader> leader;
        for (PersonalBoard personalBoard: playersPersonalBoard.values()){
            CardManager cardManager = personalBoard.getCardManager();
            for (int i = 0; i < leaderAtStart; i++){
                leader = Optional.ofNullable(deckLeader.poll());
                leader.ifPresent(x -> x.attachCardToUser(personalBoard, market));
                leader.ifPresent(cardManager::addLeader);
            }
        }
    }


    /**
     * Method popDeckDevelopmentCard return the first development Development card in the block of card
     * @param row of type int - row of the matrix
     * @param column of type int - column of the matrix
     * @return Development - the first card in the block associated with this coordinates (row, col)
     * @throws DeckDevelopmentCardException if the block of cards selected is empty
     * @throws IndexOutOfBoundsException if the coordinates are out of the matrix
     */

    public Development getDeckDevelopmentCard(int row, int column) throws DeckDevelopmentCardException, IndexOutOfBoundsException {

        if(deckDevelopment.get(row).get(column).isEmpty()){
            throw new DeckDevelopmentCardException("No development card at selection (Row: "+row+" Column: "+column+")");
        }
        Development development = deckDevelopment.get(row).get(column).get(0);
        development.attachCardToUser(playersPersonalBoard.get(currentPlayer), market);

        return development;
    }

    /**
     * Method removeDeckDevelopmentCard does the same of podDeckDevelopmentCard but without returning
     * the development card
     * @param row of type int - row to select
     * @param column of type int - column to select
     * @throws DeckDevelopmentCardException if the block of cards selected is empty
     * @throws IndexOutOfBoundsException if the coordinates are out of the matrix
     * @see GameMaster#getDeckDevelopmentCard(int, int)
     */
    private void removeDeckDevelopmentCard(int row, int column) throws DeckDevelopmentCardException, IndexOutOfBoundsException{
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

    //non credo servirà
    public void pushDeckDevelopmentCard(int row, int column, Development development) throws DeckDevelopmentCardException, IndexOutOfBoundsException {
        if (deckDevelopment.get(row).get(column).size() == depthDeckDevelopment){
            throw new DeckDevelopmentCardException("Too much card at selection (Row: " + row +
                    "Column: " + column + ")");
        }
        deckDevelopment.get(row).get(column).add(0, development);
    }

    /**
     * Method drawToken draws the token from the deckToken on the top,
     * puts it back in the bottom and then  applies its effect
     */
    public void drawToken() throws DeckDevelopmentCardException {
        Optional<Token> token = Optional.ofNullable(deckToken.poll());
        token.ifPresent(deckToken::offer);

        if (token.isPresent()){
            token.get().doActionToken(this);
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

    public DeckDevData toDeckDevData(){
        return new DeckDevData(deckDevelopment.stream()
                .map(row -> row.stream()
                        .map(singleDeck -> singleDeck.stream()
                                .map(Development::toCardDevData)
                        .collect(Collectors.toCollection(ArrayList::new)))
                    .collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    public int getPlayerPosition(String username){
        return playersTurn.indexOf(username);
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

    @Override
    public void increasePlayerFaithPoint(int faithPoints) {
        playersPersonalBoard.get(currentPlayer).getFaithTrack().movePlayer(faithPoints);
    }

    /**
     * Method discardDevelopment discard a specific number of card (num) of a certain color (color)
     * in the development deck starting from the cards of level 1 to the cards of level 3
     * @param color of type Color - target color
     * @param num of type int - number of cards to delete
     * @throws DeckDevelopmentCardException if it fails on deleting all the card required
     */
    @Override
    public void discardDevelopmentSinglePlayer(Color color, int num) throws DeckDevelopmentCardException {
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

            if (rowReached == deckDevelopment.size() && numDiscarded < num){
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
    public void increaseLorenzoFaithPosition(int pos) {
        this.playersPersonalBoard.get(NAME_LORENZO).getFaithTrack().movePlayer(pos);
    }

    @Override
    public void onDeckDevelopmentCardRemove(int row, int col) {
        try {
            removeDeckDevelopmentCard(row, col);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onTurnStateChange(TurnState turnState) {
        this.turnState = turnState;
    }


    public TurnState getTurnState() {
        return turnState;
    }

    @Override
    public void attachObserver(ModelObserver observer) {
        modelObserverList.add(observer);

    }

    @Override
    public void notifyAllObservers(Consumer<ModelObserver> consumer) {
        modelObserverList.forEach(consumer);
    }
}