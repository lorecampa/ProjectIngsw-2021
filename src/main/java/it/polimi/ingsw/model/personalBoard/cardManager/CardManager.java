package it.polimi.ingsw.model.personalBoard.cardManager;

import it.polimi.ingsw.exception.CantMakeProduction;
import it.polimi.ingsw.exception.CardWithHigherOrSameLevelAlreadyIn;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Leader;

import java.util.ArrayList;

public class CardManager {
    private ArrayList<CardSlot> cardSlots = new ArrayList<>();
    private ArrayList<Leader> leaders = new ArrayList<>();
    private Development baseProduction;
    private ArrayList<Development> productionSelected=new ArrayList<>();


    public CardManager(Development baseProduction) {
        this.baseProduction = baseProduction;
        for (int i = 0; i < 3; i++) {
            cardSlots.add(new CardSlot());
        }
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
     * @param leader u want to discard*/
    public void discardLeader(Leader leader){
        leaders.remove(leader);
        //read the rules what happen when i discar a leader card?
        //do i need to throw an exception if the leader is not contained?
    }

    /**
     * Acrivate a leader card u own
     * @param leader u want to activate
     * @throws NegativeResourceException is a resource obj value goes under 0
     * @throws CantMakeProduction
     * */
    public void activateLeader(Leader leader) throws NegativeResourceException, CantMakeProduction {
        if(leaders.contains(leader)){
            if(!leaders.get(leaders.indexOf(leader)).isActive()){
                leaders.get(leaders.indexOf(leader)).doEffects();
                leaders.get(leaders.indexOf(leader)).setActive(true);
            }
        }
    }

    /**
     * Add a development card to a Card Slot
     * @param development card i want to add
     * @param index of card slot where i want to add
     * @throws CardWithHigherOrSameLevelAlreadyIn*/
    public void addDevelopmentCardTo(Development development, int index) throws CardWithHigherOrSameLevelAlreadyIn {
        cardSlots.get(index).insertCard(development);
    }

    /**
     * Select one card to add to the selected card
     * @param lvCard the level of the card u choose
     * @param indexCardSlot from you want to select*/
    public void selectCardToProduce(int  lvCard, int indexCardSlot) throws NegativeResourceException {
        if(cardSlots.get(indexCardSlot).getCardOfLv(lvCard).checkRequirements()){
            productionSelected.add(cardSlots.get(indexCardSlot).getCardOfLv(lvCard));
        }
    }

    /**
     * Add the base production to the production selected*/
    public void addBaseProduction() throws NegativeResourceException {
        if(baseProduction.checkRequirements()){
            productionSelected.add(baseProduction);
        }
    }

    /**
     * Activate the production of all the selected dev card
     * @throws  NegativeResourceException
     * @throws  CantMakeProduction
     * */
    public void developmentProduce() throws NegativeResourceException, CantMakeProduction {
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

}
