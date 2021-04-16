package it.polimi.ingsw.model.personalBoard.cardManager;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.commonInterfaces.Observable;
import it.polimi.ingsw.commonInterfaces.Observer;
import it.polimi.ingsw.exception.CantMakeProductionException;
import it.polimi.ingsw.exception.CardAlreadyUsed;
import it.polimi.ingsw.exception.CardWithHigherOrSameLevelAlreadyIn;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Effect.State;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CardManager implements Observable {
    private final ArrayList<CardSlot> cardSlots = new ArrayList<>();

    private final ArrayList<Leader> leaders = new ArrayList<>();
    private final Development baseProduction;
    private final ArrayList<Development> devCardsUsed = new ArrayList<>();
    private final ArrayList<Leader> leadersUsed = new ArrayList<>();

    private final ArrayList<Observer> observers = new ArrayList<>();

    public CardManager() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        this.baseProduction = mapper.readValue(new File("src/main/resources/json/baseProduction.json"), Development.class);
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
     * Method to add the player Resource Manager to the base production of the player board
     * @param playerResourceManager is the player Resource Manager
     */
    public void setUpBaseProduction(ResourceManager playerResourceManager){
        baseProduction.setResourceManager(playerResourceManager);
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
        notifyAllObservers();
    }

    /**
     * Method to activate a leader card
     * @param leaderIndex is the index of the leader to activate
     * @throws IndexOutOfBoundsException if there is no leader at the leaderIndex
     * @throws CantMakeProductionException if a creation effect of the leader can't be activated
     */
    public void activateLeader(int leaderIndex) throws IndexOutOfBoundsException, CantMakeProductionException {
        Leader leader = leaders.get(leaderIndex);
        if(!leader.isActive() && leader.checkRequirements()){
            leader.doCreationEffects();
            leader.setActive(true);
        }
    }

    /**
     * Method to add a development card to a Card Slot
     * @param development is the card to add
     * @param index is the index of the card slot to add the card
     * @throws CardWithHigherOrSameLevelAlreadyIn if can't add the card due to its level
     * @throws IndexOutOfBoundsException if the card slot selected does not exist
     */
    public void addDevelopmentCardTo(Development development, int index) throws CardWithHigherOrSameLevelAlreadyIn, IndexOutOfBoundsException {
        cardSlots.get(index).insertCard(development);
    }


    /**
     * Method to activate a leader effect
     * @param leaderIndex is the index of the leader
     * @param state is the current state of the turn
     * @throws IndexOutOfBoundsException if the leader selected does not exist
     * @throws CantMakeProductionException if the leader's production effect can't be activated
     * @throws CardAlreadyUsed if the card has already been used in this turn
     */
    public void activateLeaderEffect(int leaderIndex, State state) throws IndexOutOfBoundsException, CantMakeProductionException, CardAlreadyUsed {
        Leader leader = leaders.get(leaderIndex);
        if (leadersUsed.contains(leader))
            throw new CardAlreadyUsed("Leader already used");
        leader.doEffects(state);
        leadersUsed.add(leader);
    }

    /**
     * Method to activate the production of a development card
     * @param lvCard is the level of the card
     * @param indexCardSlot is the card slot in which the card is in it
     * @throws CantMakeProductionException if the card's production can't be activated
     * @throws CardAlreadyUsed if the card has already been used in this turn
     * @throws IndexOutOfBoundsException if the card slot selected does not exist
     */
    public void developmentProduce(int  lvCard, int indexCardSlot) throws CantMakeProductionException, CardAlreadyUsed, IndexOutOfBoundsException {
        Development development = cardSlots.get(indexCardSlot).getCardOfLv(lvCard);
        if (devCardsUsed.contains(development))
            throw new CardAlreadyUsed("Card already used");
        development.doEffects(State.PRODUCTION_STATE);
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
        baseProduction.doEffects(State.PRODUCTION_STATE);
        devCardsUsed.add(baseProduction);
    }

    /**
     * Method to check if there is at least howMany card with a level equal or above level
     * @param howMany is the number of card
     * @param level is the level threshold
     */
    public boolean doIHaveDevWithLv(int howMany, int level){
        int count =0;
        for(CardSlot cardSlot: cardSlots){
            if(cardSlot.getLvReached()>=level){
                count++;
            }
        }
        return count>=howMany;
    }


    /**
     * Method doIHaveDev checks if we have in our cards slot a specific number of card with a
     * defined color and level
     * @param howMany of type int - number of card with this propriety
     * @param color of type Color - color of those cards
     * @param level of type int - level of those cards
     * @return boolean - true if you own them, otherwise false
     */
    public boolean doIHaveDev(int howMany, Color color, int level){
        int count = 0;
        for (CardSlot cardSlot: cardSlots){
            for (int i = 0; i < cardSlot.getLvReached(); i++){
                 Development dev = cardSlot.getCardOfLv(i+1);
                 if (dev.getLevel() >= level){
                     if (color == Color.ANY || dev.getColor() == color) count ++;
                 }
                 if (count >= howMany) return true;
            }
        }
        return false;
    }

    /**
     * Method to check if there is at least howMany card with color equal to color
     * @param howMany is the number of card
     * @param color is the color
     */
    public boolean doIhaveDevWithColor(int howMany, Color color){
        int count=0;
        for(CardSlot cardSlot: cardSlots){
            count+=cardSlot.howManyCardWithColor(color);
        }
        return count>=howMany;
    }

    @Override
    public void attachObserver(Observer observer) {
        if(!observers.contains(observer))
            observers.add(observer);
    }

    @Override
    public void notifyAllObservers() {
        for (Observer observer:observers){
            observer.updateFromCardManager();
        }
    }
}
