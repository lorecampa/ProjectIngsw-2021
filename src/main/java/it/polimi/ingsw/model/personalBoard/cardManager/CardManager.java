package it.polimi.ingsw.model.personalBoard.cardManager;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.commonInterfaces.Observable;
import it.polimi.ingsw.commonInterfaces.Observer;
import it.polimi.ingsw.exception.CantMakeProductionException;
import it.polimi.ingsw.exception.CardWithHigherOrSameLevelAlreadyIn;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Leader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CardManager implements Observable {
    private final ArrayList<CardSlot> cardSlots = new ArrayList<>();
    private final ArrayList<Leader> leaders = new ArrayList<>();
    private final Development baseProduction;
    private final ArrayList<Development> productionSelected=new ArrayList<>();
    private final ArrayList<Observer> observers = new ArrayList<>();

    public CardManager() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        this.baseProduction = mapper.readValue(new File("src/main/resources/json/baseProduction.json"), Development.class);
        for (int i = 0; i < 3; i++) {
            cardSlots.add(new CardSlot());
        }
    }

    public ArrayList<Leader> getLeaders() {
        return leaders;
    }

    /**
     * Set up the card manager to be ready for the curr turn*/
    public void newTurn(){
        productionSelected.clear();
    }

    /**
     * Add a leader to this Card manager
     * @param leader u want to add*/
    public void addLeader(Leader leader){
        leaders.add(leader);
    }

    /**
     * Discard a leader from your hand
     * @param leaderIndex u want to discard
     */
    public void discardLeader(int leaderIndex) throws IndexOutOfBoundsException{
        leaders.remove(leaderIndex);
        notifyAllObservers();
    }

    /**
     * Activate a leader card u own
     * @param leaderIndex u want to activate
     * @throws NegativeResourceException is a resource obj value goes under 0
     */
    public void activateLeader(int leaderIndex) throws NegativeResourceException, IndexOutOfBoundsException {
        if(!leaders.get(leaderIndex).isActive()){
            leaders.get(leaderIndex).checkRequirements();
            leaders.get(leaderIndex).setActive(true);
        }
    }

    /**
     * Add a development card to a Card Slot
     * @param development card i want to add
     * @param index of card slot where i want to add
     * @throws CardWithHigherOrSameLevelAlreadyIn if can't add the card due to the level
     */
    public void addDevelopmentCardTo(Development development, int index) throws CardWithHigherOrSameLevelAlreadyIn, IndexOutOfBoundsException {
        cardSlots.get(index).insertCard(development);
    }

    /**
     * Select one card to add to the selected card
     * @param lvCard the level of the card u choose
     * @param indexCardSlot from you want to select
     */
    public void addCardToProduce(int  lvCard, int indexCardSlot) throws IndexOutOfBoundsException {
            productionSelected.add(cardSlots.get(indexCardSlot).getCardOfLv(lvCard));
    }

    /**
     * Add the base production to the production selected
     */
    public void addBaseProduction(){
            productionSelected.add(baseProduction);
    }

    public void activateLeaderEffect(int leaderIndex) throws IndexOutOfBoundsException, CantMakeProductionException, NegativeResourceException {
        leaders.get(leaderIndex).doEffects();
    }

    /**
     * Activate the production of all the selected dev card
     * @throws  NegativeResourceException
     * @throws  CantMakeProductionException
     */
    public void developmentProduce() throws NegativeResourceException, CantMakeProductionException {
        for(Development dev: productionSelected){
            dev.doEffects();
        }
    }

    /**
     * Check if i have howMany dev card at least of level in my slots
     * @param howMany card u are looking for
     * @param level of the card i'm looking for*/
    public boolean doIHaveDevWithLv(int howMany, int level){
        int count =0;
        for(CardSlot cardSlot: cardSlots){
            if(cardSlot.getLvReached()>=level){
                count+=cardSlot.getLvReached()-level;
            }
        }
        return count>=howMany;
    }

    /**
     * Check if i have howMany dev card at least of color in my slots
     * @param howMany card u are looking for
     * @param color of the card i'm looking for*/
    public boolean doIhaveDevWithColor(int howMany, Color color){
        int count=0;
        for(CardSlot cardSlot: cardSlots){
            count+=cardSlot.howManyCardWithColor(color);
        }
        return count>=howMany;
    }

    @Override
    public void attachObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyAllObservers() {
        for (Observer observer:observers){
            observer.updateFromCardManager();
        }
    }
}
