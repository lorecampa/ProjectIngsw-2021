package it.polimi.ingsw.model.personalBoard.cardManager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.CardDevData;
import it.polimi.ingsw.client.data.CardLeaderData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.PlayerState;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Effect.Activation.MarbleEffect;
import it.polimi.ingsw.model.card.Effect.Activation.ProductionEffect;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.observer.CardManagerObserver;
import it.polimi.ingsw.observer.GameMasterObservable;
import it.polimi.ingsw.observer.GameMasterObserver;
import it.polimi.ingsw.observer.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Card Manager is a class that manage all the player's action with a card.
 */
public class CardManager extends GameMasterObservable implements Observable<CardManagerObserver> {
    @JsonIgnore
    List<CardManagerObserver> cardManagerObserverList = new ArrayList<>();

    private final ArrayList<CardSlot> cardSlots = new ArrayList<>();
    private final ArrayList<Leader> leaders = new ArrayList<>();
    private final Development baseProduction;
    private final ArrayList<Development> devCardsUsed = new ArrayList<>();
    private final ArrayList<Leader> leadersUsed = new ArrayList<>();
    private int indexCardSlotBuffer;
    private int rowDeckBuffer;
    private int colDeckBuffer;

    /**
     * Construct a Card Manager with a specific base production.
     * @param baseProduction the base production of the board.
     */
    @JsonCreator
    public CardManager(@JsonProperty("baseProduction") Development baseProduction){
        this.baseProduction = baseProduction;
        for (int i = 0; i < 3; i++) {
            cardSlots.add(new CardSlot());
        }
    }

    /**
     * Return the ArrayList that contains all the leader cards.
     * @return the ArrayList that contains all the leader cards.
     */
    public ArrayList<Leader> getLeaders() {
        return leaders;
    }

    /**
     * Set up the card manager to be ready for the current turn.
     */
    public void restoreCM(){
        devCardsUsed.clear();
        leadersUsed.clear();
    }

    /**
     * Restore the card manager based on parameters.
     * @param personalBoard the player's personal board.
     * @param market the game's market.
     */
    public void restoreCardsManagerReference(PersonalBoard personalBoard, Market market){
        baseProduction.attachCardToUser(personalBoard, market);
        leaders.forEach(x -> x.attachCardToUser(personalBoard, market));
        for (CardSlot cs: cardSlots){
            for (int i = 0; i < cs.getLvReached(); i++){
                cs.getDevelopment(i).attachCardToUser(personalBoard, market);
            }
        }
    }

    /**
     * Add a leader to Card Manager.
     * @param leader the leader to add.
     */
    public void addLeader(Leader leader){
        leaders.add(leader);
    }

    /**
     * Discard the leader at the given leader index.
     * @param leaderIndex the index of the leader to discard.
     * @throws IndexOutOfBoundsException if there is no leader at the leaderIndex.
     * @throws InvalidStateActionException if the player is in an invalid state for the action.
     * @throws LeaderCardAlreadyActivatedException if the leader is active.
     */
    public void discardLeader(int leaderIndex) throws IndexOutOfBoundsException, InvalidStateActionException, LeaderCardAlreadyActivatedException {
        checkPlayerState(PlayerState.LEADER_MANAGE_BEFORE, PlayerState.LEADER_MANAGE_AFTER);

        Leader leaderToDiscard = leaders.get(leaderIndex);

        if (!leaderToDiscard.isActive()){
            leaders.remove(leaderIndex);
            notifyGameMaster(GameMasterObserver::discardLeader);
            notifyAllObservers(x -> x.leaderDiscard(leaderIndex));
        }else{
            throw new LeaderCardAlreadyActivatedException("Can't discard a activated leader!");
        }
    }

    /**
     * Discard the leader at the given leader index during the sut up phase.
     * @param leaderIndex the index of the leader to discard.
     * @throws IndexOutOfBoundsException if there is no leader at the leaderIndex.
     */
    public void discardLeaderSetUp(int leaderIndex) throws IndexOutOfBoundsException{
        leaders.remove(leaderIndex);
        notifyAllObservers(x -> x.leaderDiscard(leaderIndex));
    }

    /**
     * Activate a leader card.
     * @param leaderIndex the index of the leader to activate.
     * @throws IndexOutOfBoundsException if there is no leader at the leaderIndex.
     * @throws LeaderCardAlreadyActivatedException if the player has already activated the leader.
     * @throws NotEnoughRequirementException if the player doesn't have enough resources/cards to satisfy the requirements.
     * @throws InvalidStateActionException if the player is in an invalid state for the action.
     */
    public void activateLeader(int leaderIndex) throws IndexOutOfBoundsException, LeaderCardAlreadyActivatedException, NotEnoughRequirementException, InvalidStateActionException {
        checkPlayerState(PlayerState.LEADER_MANAGE_BEFORE, PlayerState.LEADER_MANAGE_AFTER);

        Leader leader = leaders.get(leaderIndex);
        if (leader.isActive()){
            throw new LeaderCardAlreadyActivatedException("Leader card is already activated");
        }
        leader.checkRequirements();
        leader.doCreationEffects();

        int i=0;
        while (leaders.get(i).isActive()){
            i++;
        }

        leaders.remove(leader);
        leaders.add(i, leader);

        leader.setActive();

        notifyAllObservers(x -> x.leaderActivated(leaders));
    }

    /**
     * Add a development card to a Card Slot.
     * @param development the card to add.
     * @param indexCardSlot the index of the card slot to add the card.
     * @throws CardWithHigherOrSameLevelAlreadyIn if can't add the card due to its level.
     * @throws IndexOutOfBoundsException if the card slot selected does not exist.
     * @throws InvalidStateActionException if the player is in an invalid state for the action.
     */
    public void addDevCardTo(Development development, int indexCardSlot) throws CardWithHigherOrSameLevelAlreadyIn, IndexOutOfBoundsException, InvalidStateActionException {
        checkPlayerState(PlayerState.LEADER_MANAGE_BEFORE);

        cardSlots.get(indexCardSlot).insertCard(development);
        indexCardSlotBuffer = indexCardSlot;

    }

    /**
     * Set the row and column of deck buffer.
     * @param row the value of the row.
     * @param col the value of the columns.
     */
    public void setDeckBufferInfo(int row, int col){
        rowDeckBuffer = row;
        colDeckBuffer = col;
    }

    /**
     * Add a development card to card slot.
     */
    public void emptyCardSlotBuffer(){
        cardSlots.get(indexCardSlotBuffer).emptyBuffer();
        notifyGameMaster(x -> x.onPlayerStateChange(PlayerState.LEADER_MANAGE_AFTER));
        notifyGameMaster(x -> x.onDeckDevelopmentCardRemove(rowDeckBuffer, colDeckBuffer));
        notifyAllObservers(x -> x.cardSlotUpdate(indexCardSlotBuffer, rowDeckBuffer, colDeckBuffer));
        if (howManyCardDoIOwn() == 7){
            notifyGameMaster(GameMasterObserver::winningCondition);
        }
    }

    /**
     * Activate a leader effect.
     * @param leaderIndex the index of the leader.
     * @param playerState the current state of the turn.
     * @throws IndexOutOfBoundsException if the leader selected does not exist
     * @throws CardAlreadyUsed if the card has already been used in this turn
     * @throws NotEnoughRequirementException if the player doesn't have enough resources/cards to satisfy the requirements.
     * @throws InvalidStateActionException if the player is in an invalid state for the action.
     */
    public void activateLeaderEffect(int leaderIndex, PlayerState playerState) throws IndexOutOfBoundsException, CardAlreadyUsed, NotEnoughRequirementException, InvalidStateActionException {
        checkPlayerState(PlayerState.LEADER_MANAGE_BEFORE,
                PlayerState.WHITE_MARBLE_CONVERSION,
                PlayerState.PRODUCTION_ACTION,
                PlayerState.LEADER_MANAGE_AFTER);

        Leader leader = leaders.get(leaderIndex);
        if (leadersUsed.contains(leader))
            throw new CardAlreadyUsed("Leader already used");
        leader.doActivationEffects(playerState);
        leadersUsed.add(leader);
    }

    /**
     * Activate a leader effect without consume it's use.
     * @param leaderIndex the index of the leader.
     * @param playerState the current state of the turn.
     * @throws InvalidStateActionException if the player is in an invalid state for the action.
     * @throws NotEnoughRequirementException if the player doesn't have enough resources/cards to satisfy the requirements.
     */
    public void activateLeaderInfinite(int leaderIndex, PlayerState playerState) throws InvalidStateActionException, NotEnoughRequirementException {
        checkPlayerState(PlayerState.WHITE_MARBLE_CONVERSION);
        Leader leader = leaders.get(leaderIndex);
        leader.doActivationEffects(playerState);
    }

    /**
     * Activate the production of a development card.
     * @param indexCardSlot the card slot in which the card is in it.
     * @throws CardAlreadyUsed if the card has already been used in this turn.
     * @throws IndexOutOfBoundsException if the card slot selected does not exist.
     * @throws NotEnoughRequirementException if the player doesn't have enough resources/cards to satisfy the requirements.
     * @throws  InvalidStateActionException if the player is in an invalid state for the action.
     */
    public void developmentProduce(int indexCardSlot) throws CardAlreadyUsed, IndexOutOfBoundsException, NotEnoughRequirementException, InvalidStateActionException {
        checkPlayerState(PlayerState.LEADER_MANAGE_BEFORE, PlayerState.PRODUCTION_ACTION);

        Development development = cardSlots.get(indexCardSlot).getLastInsertedCard();
        if (devCardsUsed.contains(development))
            throw new CardAlreadyUsed("Card already used");

        development.doActivationEffects(PlayerState.PRODUCTION_ACTION);
        devCardsUsed.add(development);
    }

    /**
     * Activate the base production of the player board.
     * @throws CardAlreadyUsed if the base production has already been used in this turn.
     * @throws NotEnoughRequirementException if the player doesn't have enough resources/cards to satisfy the requirements.
     * @throws InvalidStateActionException if the player is in an invalid state for the action.
     */
    public void baseProductionProduce() throws CardAlreadyUsed, NotEnoughRequirementException, InvalidStateActionException {
        checkPlayerState(PlayerState.LEADER_MANAGE_BEFORE, PlayerState.PRODUCTION_ACTION);

        if (devCardsUsed.contains(baseProduction))
            throw new CardAlreadyUsed("Base Production already used");

        baseProduction.doActivationEffects(PlayerState.PRODUCTION_ACTION);
        devCardsUsed.add(baseProduction);
    }


    /**
     * Checks if we have in our cards slot a specific number of card with a defined color and level.
     * @param howMany the number of card with this propriety.
     * @param color the color of those cards, ANY if not specified.
     * @param level the level of those cards.
     * @throws NotEnoughRequirementException if the player doesn't have enough resources/cards to satisfy the requirements.
     */
    public void doIHaveDev(int howMany, Color color, int level) throws NotEnoughRequirementException {
        int count = 0;
        for (CardSlot cardSlot: cardSlots){
            for (int i = 0; i < cardSlot.getLvReached(); i++){
                 Development dev = cardSlot.getDevelopment(i);
                 if (level <= 0 || dev.getLevel() == level){
                     if (color == Color.ANY || dev.getColor() == color) count ++;
                 }
                 if (count >= howMany)
                     return;
            }
        }
        throw new NotEnoughRequirementException("You don't have enough card requirement");
    }

    /**
     * Return how many card the player own.
     * @return how many card the player own.
     */
    private int howManyCardDoIOwn(){
        return cardSlots.stream().mapToInt(CardSlot::getLvReached).sum();
    }

    /**
     * Return the sum af all victory points of all the cards.
     * @return the sum af all victory points of all the cards.
     */
    public int getVictoryPointsCard(){
        int vp;
        vp=cardSlots.stream().mapToInt(CardSlot::getVictoryPoint).sum();
        vp+=leaders.stream().filter(Leader::isActive).mapToInt(Leader::getVictoryPoints).sum();
        return vp;
    }

    /**
     * Return a map that contains all the marble effects.
     * @return a map that contains all the marble effects.
     */
    public Map<Integer, ArrayList<ResourceData>> mapOfMarbleEffect(){
        return leaders.stream()
                .filter(Leader::isActive)
                .filter(x -> x.getOnActivationEffects().stream().anyMatch(effect -> effect instanceof MarbleEffect))
                .collect(Collectors.toMap(leaders::indexOf,
                        x -> x.getOnActivationEffects().stream()
                                .filter(y -> y instanceof MarbleEffect)
                                .map(effect -> (MarbleEffect) effect)
                                .map(MarbleEffect::getTransformIn)
                                .flatMap(y -> y.stream().map(Resource::toClient))
                                .collect(Collectors.toCollection(ArrayList::new))));
    }

    /**
     * Return the number of marble effects.
     * @return the number of marble effects.
     */
    public int howManyMarbleEffects(){
        return  leaders.stream()
                .filter(Leader::isActive)
                .map(Card::getOnActivationEffects)
                .flatMap(ArrayList::stream)
                .filter(x -> x instanceof MarbleEffect)
                .mapToInt(x -> 1)
                .sum();
    }

    /**
     * Return the number of production effects.
     * @return the number of production effects.
     */
    public int howManyProductionEffects(){
        return leaders.stream()
                .filter(Leader::isActive)
                .map(Card::getOnActivationEffects)
                .flatMap(ArrayList::stream)
                .filter(x -> x instanceof ProductionEffect)
                .mapToInt(x -> 1)
                .sum();
    }

    /**
     * Return a matrix of CardDevData based on the card in card slots.
     * @return a matrix of CardDevData based on the card in card slots.
     */
    public ArrayList<ArrayList<CardDevData>> toCardSlotsData(){
        ArrayList<ArrayList<CardDevData>> cardSlotsData = new ArrayList<>();
        for (CardSlot cardSlot : cardSlots){
            cardSlotsData.add(cardSlot.toCardSlotData());
        }
        return cardSlotsData;
    }

    /**
     * Return an ArrayList of CardLeaderData based on the leaders.
     * @return an ArrayList of CardLeaderData based on the leaders.
     */
    public ArrayList<CardLeaderData> toLeadersData(){
        return leaders.stream().map(Leader::toCardLeaderData).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void attachObserver(CardManagerObserver observer) {
        cardManagerObserverList.add(observer);
    }

    @Override
    public void notifyAllObservers(Consumer<CardManagerObserver> consumer) {
        cardManagerObserverList.forEach(consumer);
    }
}
