package it.polimi.ingsw.model.personalBoard.cardManager;

import it.polimi.ingsw.exception.CardWithHigherOrSameLevelAlreadyIn;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;

import java.util.ArrayList;

public class CardSlot {
    private final ArrayList<Development> developments = new ArrayList<>();

    /**
     * Method to get the higher lv present in this slot
     */
    public int getLvReached() {
        return developments.size();
    }

    /**
     * Method to insert a card in CardSlot
     * @param newCard is the card I want to insert
     * @throws  CardWithHigherOrSameLevelAlreadyIn if the card lv is already in the CardSlot
     */
    public void insertCard(Development newCard) throws CardWithHigherOrSameLevelAlreadyIn {
        if(developments.size() + 1 != newCard.getLevel()){
            throw new CardWithHigherOrSameLevelAlreadyIn("This card slot already contain that lv or higher!");
        }
        developments.add(newCard);
    }

    /**
     * Method to get a specific card
     * @param level of the card i want
     * @return the card i'm asking for
     * @throws IndexOutOfBoundsException if the level selected does not exist
     */
    public Development getCardOfLv(int level) throws IndexOutOfBoundsException{
        return developments.get(level-1);
    }

    /**
     * Method to get the amount of card of a specify color
     * @param color is the color I'm searching
     */
    public int howManyCardWithColor(Color color){
        int count=0;
        for(Development dev: developments){
            if(dev.getColor().equals(color)){
                count++;
            }
        }
        return count;
    }
}
