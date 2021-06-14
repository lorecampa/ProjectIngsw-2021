package it.polimi.ingsw.model.personalBoard.cardManager;

import it.polimi.ingsw.client.data.CardDevData;
import it.polimi.ingsw.exception.CardWithHigherOrSameLevelAlreadyIn;
import it.polimi.ingsw.model.card.Development;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Card Slot defines a card slot in which a player can insert Development cards.
 */
public class CardSlot {
    private final ArrayList<Development> developments = new ArrayList<>();
    private Development buffer = null;

    /**
     * Return the level reached in the card slot.
     * @return the level reached in the card slot.
     */
    public int getLvReached() {
        return developments.size();
    }

    /**
     * Insert a card in the card slot.
     * @param newCard is the card I want to insert
     * @throws  CardWithHigherOrSameLevelAlreadyIn if the card level is already in the card slot.
     */
    public void insertCard(Development newCard) throws CardWithHigherOrSameLevelAlreadyIn {
        if(developments.size() + 1 != newCard.getLevel()){
            throw new CardWithHigherOrSameLevelAlreadyIn("This card slot already contain that lv or higher!");
        }
        buffer= newCard;
    }

    /**
     * Add the card in buffer in the card slot and empty the buffer.
     */
    public void emptyBuffer(){
        if (buffer != null){
            developments.add(buffer);
            buffer = null;
        }
    }

    /**
     * Return an ArrayList of CardDevData based on the development cards in the card slots.
     * @return an ArrayList of CardDevData based on the development cards in the card slots.
     */
    public ArrayList<CardDevData> toCardSlotData(){
        return developments.stream().map(Development::toCardDevData).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Return the development card at the index in the card slot.
     * @param index the index of the development card in the card slot.
     * @return the development card at the index in the card slot.
     */
    public Development getDevelopment(int index) {
        return developments.get(index);
    }

    /**
     * Return the last inserted card in the card slot.
     * @return the last inserted card in the card slot.
     */
    public Development getLastInsertedCard(){
        return developments.get(getLvReached() - 1);
    }

    /**
     * Return the sum of victory points of the cards in the card slot.
     * @return the sum of victory points of the cards in the card slot.
     */
    public int getVictoryPoint(){
        return developments.stream().mapToInt(Development::getVictoryPoints).sum();
    }
}
