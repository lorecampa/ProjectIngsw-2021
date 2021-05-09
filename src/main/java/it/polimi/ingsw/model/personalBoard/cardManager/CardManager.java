package it.polimi.ingsw.model.personalBoard.cardManager;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.card.Effect.Activation.MarbleEffect;
import it.polimi.ingsw.model.card.Effect.Activation.ProductionEffect;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.observer.CardManagerObserver;
import it.polimi.ingsw.observer.GameMasterObservable;
import it.polimi.ingsw.observer.GameMasterObserver;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CardManager extends GameMasterObservable implements Observable<CardManagerObserver> {

    List<CardManagerObserver> cardManagerObserverList = new ArrayList<>();
    private final ArrayList<CardSlot> cardSlots = new ArrayList<>();
    private final ArrayList<Leader> leaders = new ArrayList<>();
    private final Development baseProduction;
    private final ArrayList<Development> devCardsUsed = new ArrayList<>();
    private final ArrayList<Leader> leadersUsed = new ArrayList<>();

    private int indexCardSlotBuffer;
    private int rowDeckDevelopmentBuffer;
    private int colDeckDevelopmentBuffer;


    public CardManager(Development baseProduction) throws IOException {
        this.baseProduction = baseProduction;
        for (int i = 0; i < 3; i++) {
            cardSlots.add(new CardSlot());
        }
    }

    /**
     * Method to set up the card manager to be ready for the current turn
     */
    public void clearUsed(){
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
    //TODO ask
    //if we discard starter leader card at runtime we increase others faith track position and it is wrong
    public void discardLeader(int leaderIndex) throws IndexOutOfBoundsException{
        leaders.remove(leaderIndex);
        notifyGameMasterObserver(GameMasterObserver::discardLeader);
        notifyAllObservers(x -> x.leaderManage(leaderIndex, true));
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
        if (!leader.checkRequirements()){
            throw new NotEnoughRequirementException("You don't have enough requirement to activate this leader card");
        }

        try{
            leader.doCreationEffects();
        }catch (CantMakeProductionException e){
            //it will never be thrown
        }
        leader.setActive(true);
        notifyAllObservers(x -> x.leaderManage(leaderIndex, false));

    }

    /**
     * Method to add a development card to a Card Slot
     * @param development is the card to add
     * @param indexCardSlot is the index of the card slot to add the card
     * @throws CardWithHigherOrSameLevelAlreadyIn if can't add the card due to its level
     * @throws IndexOutOfBoundsException if the card slot selected does not exist
     */
    public void addDevelopmentCardTo(Development development, int indexCardSlot) throws CardWithHigherOrSameLevelAlreadyIn, IndexOutOfBoundsException {
        cardSlots.get(indexCardSlot).insertCard(development);
        indexCardSlotBuffer = indexCardSlot;
    }

    public void setDeckDevelopmentCardBufferInformation(int row, int col){
        rowDeckDevelopmentBuffer = row;
        colDeckDevelopmentBuffer = col;
    }

    public void emptyCardSlotBuffer(){
        cardSlots.get(indexCardSlotBuffer).emptyBuffer();

        notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.LEADER_MANAGE_AFTER));

        //remove card from the server deck
        notifyGameMasterObserver(x ->
                x.onDeckDevelopmentCardRemove(rowDeckDevelopmentBuffer, colDeckDevelopmentBuffer));

        notifyAllObservers(x ->
                x.cardSlotUpdate(indexCardSlotBuffer,
                rowDeckDevelopmentBuffer,
                colDeckDevelopmentBuffer));
    }



    /**
     * Method to activate a leader effect
     * @param leaderIndex is the index of the leader
     * @param turnState is the current state of the turn
     * @throws IndexOutOfBoundsException if the leader selected does not exist
     * @throws CantMakeProductionException if the leader's production effect can't be activated
     * @throws CardAlreadyUsed if the card has already been used in this turn
     */
    public void activateLeaderEffect(int leaderIndex, TurnState turnState) throws IndexOutOfBoundsException, CantMakeProductionException, CardAlreadyUsed {
        Leader leader = leaders.get(leaderIndex);
        if (leadersUsed.contains(leader))
            throw new CardAlreadyUsed("Leader already used");
        leader.doEffects(turnState);
        leadersUsed.add(leader);
    }

    /**
     * Method to activate the production of a development card
     * @param indexCardSlot is the card slot in which the card is in it
     * @throws CantMakeProductionException if the card's production can't be activated
     * @throws CardAlreadyUsed if the card has already been used in this turn
     * @throws IndexOutOfBoundsException if the card slot selected does not exist
     */
    public void developmentProduce(int indexCardSlot) throws CantMakeProductionException, CardAlreadyUsed, IndexOutOfBoundsException {
        Development development = cardSlots.get(indexCardSlot).getLastInsertedCard();
        if (devCardsUsed.contains(development))
            throw new CardAlreadyUsed("Card already used");

        notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.PRODUCTION_ACTION));

        development.doEffects(TurnState.PRODUCTION_ACTION);
        devCardsUsed.add(development);


    }

    /**
     * Method to activate the base production of the player board
     * @throws CardAlreadyUsed if the base production has already been used in this turn
     * @throws CantMakeProductionException if the base production's production can't be activated
     */
    public void baseProductionProduce() throws CardAlreadyUsed, CantMakeProductionException {
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
     * @return boolean - true if you own them, otherwise false
     */
    public boolean doIHaveDev(int howMany, Color color, int level){
        int count = 0;
        for (CardSlot cardSlot: cardSlots){
            for (int i = 0; i < cardSlot.getLvReached(); i++){
                 Development dev = cardSlot.getDevelopment(i);
                 if (dev.getLevel() == level || level <= 0){
                     if (color == Color.ANY || dev.getColor() == color) count ++;
                 }
                 if (count >= howMany)
                     return true;
            }
        }
        return false;
    }

    //serve un metodo del genere in quanto posso avere carte con più effetti
    //vedere come gestire la cosa in quanto forse avrebbe più senso avere
    //un array anche con gli effetti lato client

    public boolean doIHaveMarbleEffects(){
        return leaders.stream()
                .map(Card::getOnActivationEffects)
                .flatMap(ArrayList::stream)
                .anyMatch(x -> x instanceof MarbleEffect);
    }

    //uguale come sopra
    public boolean doIHaveProductionEffects(){
        return leaders.stream()
                .map(Card::getOnActivationEffects)
                .flatMap(ArrayList::stream)
                .anyMatch(x -> x instanceof ProductionEffect);
    }

    /*
    metodo che andrà nel client per cercare il numero di leader con una determinato numero di palline bianche

    public HashMap<Integer, ArrayList<ResourceData>> whiteMarbleLeaderTransformation(){
        HashMap<Integer, ArrayList<ResourceData>> assignment = new HashMap<>();
        for(Leader leader: leaders){
            if (leader.isActive()){
                for (Effect effect: leader.getOnActivationEffects()){
                    if (effect instanceof MarbleEffect){
                        ArrayList<ResourceData> transformInData =
                                ((MarbleEffect) effect).getTransformIn().stream()
                                .map(Resource::toClient).collect(Collectors.toCollection(ArrayList::new));

                        assignment.put(leaders.indexOf(leader),
                                transformInData);
                    }
                }
            }
        }
        return assignment;
    }

     */



    @Override
    public void attachObserver(CardManagerObserver observer) {
        cardManagerObserverList.add(observer);
    }

    @Override
    public void notifyAllObservers(Consumer<CardManagerObserver> consumer) {
        cardManagerObserverList.forEach(consumer);
    }
}
