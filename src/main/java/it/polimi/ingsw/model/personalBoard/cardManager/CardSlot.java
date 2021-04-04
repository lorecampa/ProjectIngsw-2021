package it.polimi.ingsw.model.personalBoard.cardManager;

import it.polimi.ingsw.exception.CardWithHigherOrSameLevelAlreadyIn;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;

import java.util.ArrayList;

public class CardSlot {
    private ArrayList<Development> developments = new ArrayList<>();

    public CardSlot() {

    }

    /**
     * Get the higher lv present in this slot*/
    public int getLvReached() {
        return developments.size();
    }

    /**
     * Insert a card in CardSlot
     * @param newCard the card i want to add
     * @throws  CardWithHigherOrSameLevelAlreadyIn if the card lv is already in the CardSlot*/
    public void insertCard(Development newCard) throws CardWithHigherOrSameLevelAlreadyIn {
        if(developments.size() >= 2/*TODO: LOLLO->newCard.getLevel()*/){
            throw new CardWithHigherOrSameLevelAlreadyIn("This card slot already contain that lv or higher!");
        }
        developments.add(newCard);
    }

    /**
     * Get a specific card
     * @param level of the card i want
     * @return the card i'm asking for*/
    public Development getCardOfLv(int level){
        return developments.get(level-1);
    }

    /**
     * The amount of card of a specify color
     * @param color i'm looking for*/
    public int howManyCardWithColor(Color color){
        int cont=0;
        for(Development dev: developments){
            if(/*TODO: LOLLO dev.getColor()==color*/true){
                cont++;
            }
        }
        return cont;
    }
}
