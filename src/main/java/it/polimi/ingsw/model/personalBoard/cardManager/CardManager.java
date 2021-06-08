package it.polimi.ingsw.model.personalBoard.cardManager;

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
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.observer.CardManagerObserver;
import it.polimi.ingsw.observer.GameMasterObservable;
import it.polimi.ingsw.observer.GameMasterObserver;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CardManager extends GameMasterObservable implements Observable<CardManagerObserver> {
    List<CardManagerObserver> cardManagerObserverList = new ArrayList<>();

    private final ArrayList<CardSlot> cardSlots = new ArrayList<>();
    private final ArrayList<Leader> leaders = new ArrayList<>();
    private final Development baseProduction;
    private final ArrayList<Development> devCardsUsed = new ArrayList<>();
    private final ArrayList<Leader> leadersUsed = new ArrayList<>();

    private int indexCardSlotBuffer;
    private int rowDeckBuffer;
    private int colDeckBuffer;


    public CardManager(Development baseProduction) throws IOException {
        this.baseProduction = baseProduction;
        for (int i = 0; i < 3; i++) {
            cardSlots.add(new CardSlot());
        }
    }

    public ArrayList<Leader> getLeaders() {
        return leaders;
    }

    /**
     * Method to set up the card manager to be ready for the current turn
     */
    public void restoreCM(){
        devCardsUsed.clear();
        leadersUsed.clear();
    }

    /**
     * Method to add a leader to Card Manager
     * @param leader is the leader to add
     */
    public void addLeader(Leader leader){
        leaders.add(leader);
    }

    /**
     * Method to discard a leader
     * @param leaderIndex is the index of the leader to discard
     * @throws IndexOutOfBoundsException if there is no leader at the leaderIndex
     */
    public void discardLeader(int leaderIndex) throws IndexOutOfBoundsException, InvalidStateActionException, LeaderCardAlreadyActivatedException {
        checkPlayerState(PlayerState.LEADER_MANAGE_BEFORE, PlayerState.LEADER_MANAGE_AFTER);

        Leader leaderToDiscard = leaders.get(leaderIndex);

        if (!leaderToDiscard.isActive()){
            leaders.remove(leaderIndex);
            //leaderToDiscard.discardCreationEffects();
            notifyGameMaster(GameMasterObserver::discardLeader);
            notifyAllObservers(x -> x.leaderDiscard(leaderIndex));
        }else{
            throw new LeaderCardAlreadyActivatedException("Can't discard a activated leader!");
        }
    }

    public void discardLeaderSetUp(int leaderIndex) throws IndexOutOfBoundsException{
        leaders.remove(leaderIndex);
        notifyAllObservers(x -> x.leaderDiscard(leaderIndex));
    }

    /**
     * Method to activate a leader card
     * @param leaderIndex is the index of the leader to activate
     * @throws IndexOutOfBoundsException if there is no leader at the leaderIndex
     */
    public void activateLeader(int leaderIndex) throws IndexOutOfBoundsException, LeaderCardAlreadyActivatedException, NotEnoughRequirementException, InvalidStateActionException {
        checkPlayerState(PlayerState.LEADER_MANAGE_BEFORE, PlayerState.LEADER_MANAGE_AFTER);

        Leader leader = leaders.get(leaderIndex);
        if (leader.isActive()){
            throw new LeaderCardAlreadyActivatedException("Leader card is already activated");
        }
        leader.checkRequirements();
        leader.doCreationEffects();
        leader.setActive();

        notifyAllObservers(x -> x.leaderActivated(leader, leaderIndex));

    }

    /**
     * Method to add a development card to a Card Slot
     * @param development is the card to add
     * @param indexCardSlot is the index of the card slot to add the card
     * @throws CardWithHigherOrSameLevelAlreadyIn if can't add the card due to its level
     * @throws IndexOutOfBoundsException if the card slot selected does not exist
     */
    public void addDevCardTo(Development development, int indexCardSlot) throws CardWithHigherOrSameLevelAlreadyIn, IndexOutOfBoundsException, InvalidStateActionException {
        checkPlayerState(PlayerState.LEADER_MANAGE_BEFORE);

        cardSlots.get(indexCardSlot).insertCard(development);
        indexCardSlotBuffer = indexCardSlot;

    }

    public void setDeckBufferInfo(int row, int col){
        rowDeckBuffer = row;
        colDeckBuffer = col;
    }

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
     * Method to activate a leader effect
     * @param leaderIndex is the index of the leader
     * @param playerState is the current state of the turn
     * @throws IndexOutOfBoundsException if the leader selected does not exist
     * @throws CardAlreadyUsed if the card has already been used in this turn
     */
    public void activateLeaderEffect(int leaderIndex, PlayerState playerState) throws IndexOutOfBoundsException, CardAlreadyUsed, NotEnoughRequirementException, InvalidStateActionException {
        checkPlayerState(PlayerState.LEADER_MANAGE_BEFORE,
                PlayerState.WHITE_MARBLE_CONVERSION,
                PlayerState.PRODUCTION_ACTION,
                PlayerState.LEADER_MANAGE_AFTER);

        Leader leader = leaders.get(leaderIndex);
        if (leadersUsed.contains(leader))
            throw new CardAlreadyUsed("Leader already used");
        leader.doEffects(playerState);
        leadersUsed.add(leader);
    }

    public void activateLeaderInfinite(int leaderIndex, PlayerState playerState) throws InvalidStateActionException, NotEnoughRequirementException {
        checkPlayerState(PlayerState.WHITE_MARBLE_CONVERSION);
        Leader leader = leaders.get(leaderIndex);
        leader.doEffects(playerState);
    }

    /**
     * Method to activate the production of a development card
     * @param indexCardSlot is the card slot in which the card is in it
     * @throws CardAlreadyUsed if the card has already been used in this turn
     * @throws IndexOutOfBoundsException if the card slot selected does not exist
     */
    public void developmentProduce(int indexCardSlot) throws CardAlreadyUsed, IndexOutOfBoundsException, NotEnoughRequirementException, InvalidStateActionException {
        checkPlayerState(PlayerState.LEADER_MANAGE_BEFORE, PlayerState.PRODUCTION_ACTION);

        Development development = cardSlots.get(indexCardSlot).getLastInsertedCard();
        if (devCardsUsed.contains(development))
            throw new CardAlreadyUsed("Card already used");

        development.doEffects(PlayerState.PRODUCTION_ACTION);
        devCardsUsed.add(development);


    }

    /**
     * Method to activate the base production of the player board
     * @throws CardAlreadyUsed if the base production has already been used in this turn
     */
    public void baseProductionProduce() throws CardAlreadyUsed, NotEnoughRequirementException, InvalidStateActionException {
        checkPlayerState(PlayerState.LEADER_MANAGE_BEFORE, PlayerState.PRODUCTION_ACTION);

        if (devCardsUsed.contains(baseProduction))
            throw new CardAlreadyUsed("Base Production already used");

        baseProduction.doEffects(PlayerState.PRODUCTION_ACTION);
        devCardsUsed.add(baseProduction);
    }


    /**
     * Method doIHaveDev checks if we have in our cards slot a specific number of card with a
     * defined color and level
     * @param howMany of type int - number of card with this propriety
     * @param color of type Color - color of those cards, ANY if not specified
     * @param level of type int - level of those cards
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

    private int howManyCardDoIOwn(){
        return cardSlots.stream().mapToInt(CardSlot::getLvReached).sum();
    }

    public int getVictoryPointsCard(){
        int vp;
        vp=cardSlots.stream().mapToInt(CardSlot::getVictoryPoint).sum();
        vp+=leaders.stream().filter(Leader::isActive).mapToInt(Leader::getVictoryPoints).sum();
        return vp;
    }

    public Map<Integer, ArrayList<ResourceData>> listOfMarbleEffect(){
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


    public int howManyMarbleEffects(){
        return  leaders.stream()
                .filter(Leader::isActive)
                .map(Card::getOnActivationEffects)
                .flatMap(ArrayList::stream)
                .filter(x -> x instanceof MarbleEffect)
                .mapToInt(x -> 1)
                .sum();
    }


    public int howManyProductionEffects(){
        return leaders.stream()
                .filter(Leader::isActive)
                .map(Card::getOnActivationEffects)
                .flatMap(ArrayList::stream)
                .filter(x -> x instanceof ProductionEffect)
                .mapToInt(x -> 1)
                .sum();
    }

    public ArrayList<ArrayList<CardDevData>> toCardSlotsData(){
        ArrayList<ArrayList<CardDevData>> cardSlotsData = new ArrayList<>();
        for (CardSlot cardSlot : cardSlots){
            cardSlotsData.add(cardSlot.toCardSlotData());
        }
        return cardSlotsData;
    }

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
