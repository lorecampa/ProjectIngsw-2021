package it.polimi.ingsw.model.personalBoard.cardManager;

import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.controller.TurnState;
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
    public void newTurn(){
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
    public void discardLeader(int leaderIndex) throws IndexOutOfBoundsException{
        leaders.remove(leaderIndex);
        notifyGameMasterObserver(GameMasterObserver::discardLeader);
        notifyAllObservers(x -> x.leaderDiscard(leaderIndex));
    }

    public void discardLeaderSetUp(int leaderIndex) throws IndexOutOfBoundsException{
        leaders.remove(leaderIndex);
    }

    /**
     * Method to activate a leader card
     * @param leaderIndex is the index of the leader to activate
     * @throws IndexOutOfBoundsException if there is no leader at the leaderIndex
     */
    public void activateLeader(int leaderIndex) throws IndexOutOfBoundsException, LeaderCardAlreadyActivatedException, NotEnoughRequirementException {
        Leader leader = leaders.get(leaderIndex);
        if (leader.isActive()){
            throw new LeaderCardAlreadyActivatedException("Leader card is already activated");
        }
        leader.checkRequirements();
        leader.doCreationEffects();
        leader.setActive(true);

        notifyAllObservers(x -> x.leaderActivated(leader));

    }

    /**
     * Method to add a development card to a Card Slot
     * @param development is the card to add
     * @param indexCardSlot is the index of the card slot to add the card
     * @throws CardWithHigherOrSameLevelAlreadyIn if can't add the card due to its level
     * @throws IndexOutOfBoundsException if the card slot selected does not exist
     */
    public void addDevCardTo(Development development, int indexCardSlot) throws CardWithHigherOrSameLevelAlreadyIn, IndexOutOfBoundsException {
        cardSlots.get(indexCardSlot).insertCard(development);
        indexCardSlotBuffer = indexCardSlot;
    }

    public void setDeckBufferInfo(int row, int col){
        rowDeckBuffer = row;
        colDeckBuffer = col;
    }

    public void emptyCardSlotBuffer(){
        cardSlots.get(indexCardSlotBuffer).emptyBuffer();
        notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.LEADER_MANAGE_AFTER));
        notifyGameMasterObserver(x -> x.onDeckDevelopmentCardRemove(rowDeckBuffer, colDeckBuffer));
        notifyAllObservers(x -> x.cardSlotUpdate(indexCardSlotBuffer, rowDeckBuffer, colDeckBuffer));
    }



    /**
     * Method to activate a leader effect
     * @param leaderIndex is the index of the leader
     * @param turnState is the current state of the turn
     * @throws IndexOutOfBoundsException if the leader selected does not exist
     * @throws CardAlreadyUsed if the card has already been used in this turn
     */
    public void activateLeaderEffect(int leaderIndex, TurnState turnState) throws IndexOutOfBoundsException, CardAlreadyUsed, NotEnoughRequirementException {
        Leader leader = leaders.get(leaderIndex);
        if (leadersUsed.contains(leader))
            throw new CardAlreadyUsed("Leader already used");
        leader.doEffects(turnState);
        leadersUsed.add(leader);
    }

    /**
     * Method to activate the production of a development card
     * @param indexCardSlot is the card slot in which the card is in it
     * @throws CardAlreadyUsed if the card has already been used in this turn
     * @throws IndexOutOfBoundsException if the card slot selected does not exist
     */
    public void developmentProduce(int indexCardSlot) throws CardAlreadyUsed, IndexOutOfBoundsException, NotEnoughRequirementException {
        Development development = cardSlots.get(indexCardSlot).getLastInsertedCard();
        if (devCardsUsed.contains(development))
            throw new CardAlreadyUsed("Card already used");

        development.doEffects(TurnState.PRODUCTION_ACTION);
        devCardsUsed.add(development);


    }

    /**
     * Method to activate the base production of the player board
     * @throws CardAlreadyUsed if the base production has already been used in this turn
     */
    public void baseProductionProduce() throws CardAlreadyUsed, NotEnoughRequirementException {
        if (devCardsUsed.contains(baseProduction))
            throw new CardAlreadyUsed("Base Production already used");

        baseProduction.doEffects(TurnState.PRODUCTION_ACTION);
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


    public Map<Integer, ArrayList<ResourceData>> listOfMarbleEffect(){
        return leaders.stream()
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
                .map(Card::getOnActivationEffects)
                .flatMap(ArrayList::stream)
                .filter(x -> x instanceof MarbleEffect)
                .mapToInt(x -> 1)
                .sum();
    }


    public int howManyProductionEffects(){
        return leaders.stream()
                .map(Card::getOnActivationEffects)
                .flatMap(ArrayList::stream)
                .filter(x -> x instanceof ProductionEffect)
                .mapToInt(x -> 1)
                .sum();
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
