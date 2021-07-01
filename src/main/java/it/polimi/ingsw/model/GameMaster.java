package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.client.data.DeckDevData;
import it.polimi.ingsw.client.data.EffectData;
import it.polimi.ingsw.client.data.ModelData;
import it.polimi.ingsw.exception.InvalidStateActionException;
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
import it.polimi.ingsw.server.VirtualClient;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * GameMaster class manage all the action during a game.
 */
public class GameMaster implements GameMasterObserver,Observable<ModelObserver>, LorenzoIlMagnifico {
    @JsonIgnore
    List<ModelObserver> modelObserverList = new ArrayList<>();
    //-------------
    private Market market;
    private LinkedList<Token> deckToken;
    private final NavigableMap<String, PersonalBoard> playersPersonalBoard = new TreeMap<>();
    private LinkedList<Leader> deckLeader;
    private PlayerState playerState;
    private final static String NAME_LORENZO = "LorenzoIlMagnifico";
    private String currentPlayer = null;
    private int numberOfPlayer;
    private final ArrayList<String> playersTurn = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<Development>>> deckDevelopment;
    private int vaticanReportReached = 0;
    private int leaderAtStart;
    private boolean isLastTurn = false;
    private boolean gameEnded = false;
    private String baseProductionSerialized;
    private String faithTrackSerialized;

    @JsonCreator
    public GameMaster() {
    }

    /**
     * Construct a GameMaster for the match. It creates a new Market, deck of Leader and deck of Development card,
     * moreover if it is a single player game instantiate LorenzoIlMagnifico as a Player.
     * @param gameSetting the class that represent the game parameter.
     * @throws IOException when there are problems opening Json files when loading game information.
     */
    public GameMaster(GameSetting gameSetting, ArrayList<String> players) throws IOException{
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
     * Load the game data from the gameSetting.
     * @param gameSetting the game data.
     * @throws JsonProcessingException if some error occurs in serialization.
     */
    private void loadGameSetting(GameSetting gameSetting) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);


        baseProductionSerialized = mapper.writeValueAsString(gameSetting.getBaseProduction());
        faithTrackSerialized = mapper.writeValueAsString(gameSetting.getFaithTrack());

        deckDevelopment = gameSetting.getDeckDevelopment();
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
     * Change the current player in the game when a new turn starts.
     */
    public void nextPlayer() throws InvalidStateActionException {
        if(currentPlayer != null && !isPlayerInState(PlayerState.LEADER_MANAGE_AFTER)) {
            throw new InvalidStateActionException();
        }


        if (currentPlayer == null || numberOfPlayer == 1) {
            if(currentPlayer!= null){
                drawToken();
            }
            this.currentPlayer = playersTurn.get(0);
        }else{
            int indexOfCurr=playersTurn.indexOf(currentPlayer);
            if(indexOfCurr==numberOfPlayer-1){
                currentPlayer=playersTurn.get(0);
            }else{
                currentPlayer=playersTurn.get(indexOfCurr+1);
            }
        }

        if (isLastTurn &&  currentPlayer.equals(playersTurn.get(0))){
            gameOver();
        }else{
            onPlayerStateChange(PlayerState.LEADER_MANAGE_BEFORE);
            notifyAllObservers(x -> x.currentPlayerChange(currentPlayer));
        }
    }

    /**
     * Compute the game winner.
     */
    private void gameOver(){
        this.gameEnded = true;
        Map<Float, String> points= new HashMap<>();
        //ArrayList<Integer> pointsAlreadyToken=new ArrayList<>();
        float victoryPoints;
        for (String user : playersTurn){
            PersonalBoard pb=playersPersonalBoard.get(user);
            victoryPoints=(float)(pb.getCardManager().getVictoryPointsCard()+
                    pb.getFaithTrack().allVP() +
                    pb.getResourceManager().getVictoryPointsResource());
            while(points.containsKey(victoryPoints)){
                if(pb.getResourceManager().howManyDoIHave()>playersPersonalBoard.get(points.get(victoryPoints)).getResourceManager().howManyDoIHave()){
                    victoryPoints+=0.1f;
                }
                else{
                    victoryPoints-=0.1f;
                }
            }
            points.put(victoryPoints, user);
        }

        notifyAllObservers(x->x.weHaveAWinner(points));
    }


    /**
     * Return the number of players still active in game.
     * @return the number of players still active in game.
     */
    public int getNumActivePlayers(){
        return playersPersonalBoard.size();
    }

    /**
     * Set the gameEnded to a new value.
     * @param gameEnded the value to set gameEnded.
     */
    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    /**
     * Return the number of players.
     * @return the number of players.
     */
    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }

    /**
     * Return the market.
     * @return the market.
     */
    public Market getMarket() {
        return market;
    }

    /**
     * Return the current player.
     * @return the current player.
     */
    public String getCurrentPlayer(){
        return this.currentPlayer;
    }

    /**
     * Add the player in the list of the active players and creates for him a new personal board.
     * @param username the player username.
     * @throws IOException if there's a problem with the opening of the json file.
     */
    public void addPlayer(String username) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        FaithTrack playerFaithTrack = mapper.readValue(faithTrackSerialized, FaithTrack.class);

        ResourceManager playerResourceManager = new ResourceManager();

        Development playerBaseProduction = mapper.readValue(baseProductionSerialized, Development.class);
        playerBaseProduction.setResourceManager(playerResourceManager);
        CardManager playerCardManager = new CardManager(playerBaseProduction);

        PersonalBoard playerPersonalBoard = new PersonalBoard(username, playerFaithTrack,
                playerResourceManager, playerCardManager);

        playerPersonalBoard.attachGameMasterObserver(this);

        if (playersPersonalBoard.isEmpty())
            playerPersonalBoard.setInkwell(true);
        playersPersonalBoard.put(username, playerPersonalBoard);
    }

    /**
     * Return the personal board of a specific player.
     * @param username the player username.
     * @return PersonalBoard the player's personal board.
     */
    public PersonalBoard getPlayerPersonalBoard(String username){
        return playersPersonalBoard.get(username);
    }

    /**
     * Return the personal board of the current player.
     * @return the personal board of the current player.
     */
    public PersonalBoard getCurrentPlayerPersonalBoard(){
        return playersPersonalBoard.get(currentPlayer);
    }

    /**
     * Return an array of all the personal board
     * @return an array of all the personal board
     * */
    public ArrayList<PersonalBoard> getAllPersonalBoard(){
        Set<Map.Entry<String, PersonalBoard>> entries = playersPersonalBoard.entrySet();
        ArrayList<PersonalBoard> boards=new ArrayList<>();
        for(Map.Entry<String, PersonalBoard> entry : entries){
            boards.add(entry.getValue());
        }
        return boards;
    }

    /**
     * Return the Model Data of a specific player.
     * @param username the player username
     * @return the Model Data of a specific player.
     */
    public ModelData getPlayerModelData(String username){
        return playersPersonalBoard.get(username).toClient(username.equals(currentPlayer));
    }

    /**
     * Attach the Virtual Client to all the classes observable by the Virtual Client.
     * @param virtualClient the Virtual Client to attach.
     */
    public void attachPlayerVC(VirtualClient virtualClient){
        attachObserver(virtualClient);
        getMarket().attachObserver(virtualClient);
        playersPersonalBoard.get(virtualClient.getUsername()).attachVirtualClient(virtualClient);
    }

    /**
     * Attach the LorenzoIlMagnifico's Virtual Client to his faith track.
     * @param lorenzoIlMagnificoVC the LorenzoIlMagnifico's Virtual Client.
     */
    public void attachLorenzoIlMagnificoVC(VirtualClient lorenzoIlMagnificoVC){
        playersPersonalBoard.get(NAME_LORENZO).getFaithTrack().attachObserver(lorenzoIlMagnificoVC);
    }

    /**
     * Delivers all the initial four card to all the players in the game.
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
     * Return the first development Development card in the block of card.
     * @param row the row of the development deck.
     * @param column the column of the development deck.
     * @return the first card in the block associated with the coordinates.
     * @throws DeckDevelopmentCardException if the block of cards selected is empty.
     * @throws IndexOutOfBoundsException if the coordinates are out of the matrix bounds.
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
     * Remove the development card in the specific position of the development deck.
     * @param row the row selected.
     * @param column the column selected.
     * @throws DeckDevelopmentCardException if the block of cards selected is empty.
     * @throws IndexOutOfBoundsException if the coordinates are out of the matrix bounds.
     */
    private void removeDeckDevelopmentCard(int row, int column) throws DeckDevelopmentCardException, IndexOutOfBoundsException{
        if(deckDevelopment.get(row).get(column).isEmpty()){
            throw new DeckDevelopmentCardException("No development card at selection (Row: " + row +
                    " Column: " + column + ")");
        }
        deckDevelopment.get(row).get(column).remove(0);
    }


    /**
     * Draws the token from the deckToken on the top, puts it back in the bottom and then  applies its effect.
     */
    private void drawToken(){
        Optional<Token> token = Optional.ofNullable(deckToken.poll());
        token.ifPresent(deckToken::offer);
        token.ifPresent(value -> value.doActionToken(this));
    }

    /**
     * Return true if gameEnded is true.
     * @return true if gameEnded is true.
     */
    public boolean isGameEnded() {
        return gameEnded;
    }

    /**
     * Return the size of the deck of leaders.
     * @return the size of the deck of leaders.
     */
    public int getSizeDeckLeader() {
        return deckLeader.size();
    }

    /**
     * Return the size of the deck of token if exist else it return 0.
     * @return the size of the deck of token if exist else it return 0.
     */
    public int getSizeDeckToken() {return (deckToken != null) ? deckToken.size() : 0;}

    /**
     * Return the deck of development cards.
     * @return the deck of development cards.
     */
    public ArrayList<ArrayList<ArrayList<Development>>> getDeckDevelopment() {
        return deckDevelopment;
    }

    /**
     * Return a GameDevData based on the deck of development cards.
     * @return a GameDevData based on the deck of development cards.
     */
    public DeckDevData toDeckDevData(){
        return new DeckDevData(deckDevelopment.stream()
                .map(row -> row.stream()
                        .map(singleDeck -> singleDeck.stream()
                                .map(Development::toCardDevData)
                        .collect(Collectors.toCollection(ArrayList::new)))
                    .collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    /**
     * Return an ArrayList of EffectData based on the base production.
     * @return an ArrayList of EffectData based on the base production.
     */
    public ArrayList<EffectData> toEffectDataBasePro(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            Development playerBaseProduction = mapper.readValue(baseProductionSerialized, Development.class);
            return playerBaseProduction.toCardDevData().getEffects();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Return the player turn position.
     * @param username the player username.
     * @return the player turn position.
     */
    public int getPlayerPosition(String username){
        return playersTurn.indexOf(username);
    }

    /**
     * Return the username of LorenzoIlMagnifico.
     * @return the username of LorenzoIlMagnifico.
     */
    public static String getNameLorenzo() {
        return NAME_LORENZO;
    }

    /**
     * Restore all the reference in a match when the server load its saved status.
     */
    public void restoreReferenceAfterServerQuit(){
        market.attachGameMasterObserver(this);

        playersPersonalBoard.values().forEach(x ->{
            x.attachGameMasterObserver(this);
            x.getCardManager().restoreCardsManagerReference(x, market);
        });
    }

    /**
     * Return true if there's a column empty in the deck of development cards.
     * @return true if there's a column empty in the deck of development cards.
     */
    private boolean isDeckDevColEmpty(){
        for (int i = 0; i < deckDevelopment.get(0).size(); i++){
            boolean isColEmpty = true;
            for (ArrayList<ArrayList<Development>> arrayLists : deckDevelopment) {
                if (!arrayLists.get(i).isEmpty()) {
                    isColEmpty = false;
                    break;
                }
            }
            if (isColEmpty){
                return true;
            }
        }
        return false;
    }

    /**
     * Return the player state.
     * @return  the player state.
     */
    public PlayerState getPlayerState() {
        return playerState;
    }

    /**
     * See {@link GameMasterObserver#discardLeader()}.
     */
    @Override
    public void discardLeader() {
        playersPersonalBoard.get(currentPlayer).getFaithTrack().movePlayer(1);
    }

    /**
     * See {@link GameMasterObserver#vaticanReportReached(int)}.
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
     * See {@link GameMasterObserver#discardResources(int)}.
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
     * See {@link GameMasterObserver#increasePlayerFaithPoint(int)}.
     */
    @Override
    public void increasePlayerFaithPoint(int faithPoints) {
        playersPersonalBoard.get(currentPlayer).getFaithTrack().movePlayer(faithPoints);
    }

    /**
     * See {@link LorenzoIlMagnifico#discardDevelopmentSinglePlayer(Color, int)}.
     */
    @Override
    public void discardDevelopmentSinglePlayer(Color color, int num) {
        int rowReached = 0;
        int colorColumn = color.getColumnDeckDevelopment();
        int numDiscarded = 0;

        while(numDiscarded < num && rowReached < deckDevelopment.size()){
            try{
                removeDeckDevelopmentCard(rowReached, colorColumn);
                int finalRowReached = rowReached;
                notifyAllObservers(x -> x.removeDeckDevelopmentSinglePlayer(finalRowReached, colorColumn));
                numDiscarded++;
            }catch (DeckDevelopmentCardException e){
                rowReached++;
            }
        }
        if (isDeckDevColEmpty()){
            winningCondition();
        }
    }

    /**
     * See {@link LorenzoIlMagnifico#shuffleToken()}.
     */
    @Override
    public void shuffleToken() {
        Collections.shuffle(this.deckToken);
    }

    /**
     * See {@link LorenzoIlMagnifico#increaseLorenzoFaithPosition(int)}.
     */
    @Override
    public void increaseLorenzoFaithPosition(int pos) {
        this.playersPersonalBoard.get(NAME_LORENZO).getFaithTrack().movePlayer(pos);
    }

    /**
     * See {@link GameMasterObserver#onDeckDevelopmentCardRemove(int, int)}.
     */
    @Override
    public void onDeckDevelopmentCardRemove(int row, int col) {
        try {
            removeDeckDevelopmentCard(row, col);
        } catch (Exception ignored) {
        }
    }

    /**
     * See {@link GameMasterObserver#onPlayerStateChange(PlayerState)}.
     */
    @Override
    public void onPlayerStateChange(PlayerState playerState) {
        this.playerState = playerState;
    }

    /**
     * See {@link GameMasterObserver#isPlayerInState(PlayerState...)}.
     */
    @Override
    public boolean isPlayerInState(PlayerState... states) {
        return Arrays.stream(states).anyMatch(x -> x == playerState);
    }

    /**
     * See {@link Observable#attachObserver(Object)}.
     */
    @Override
    public void attachObserver(ModelObserver observer) {
        modelObserverList.add(observer);
    }

    /**
     * See {@link Observable#notifyAllObservers(Consumer)}.
     */
    @Override
    public void notifyAllObservers(Consumer<ModelObserver> consumer) {
        modelObserverList.forEach(consumer);
    }

    /**
     * See {@link GameMasterObserver#winningCondition()}.
     */
    @Override
    public void winningCondition() {
        if(!isLastTurn){
            notifyAllObservers(ModelObserver::winningCondition);
            this.isLastTurn = true;
        }
    }
}