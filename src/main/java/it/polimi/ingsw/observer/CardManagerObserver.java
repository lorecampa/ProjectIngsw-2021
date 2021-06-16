package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.card.Leader;

import java.util.ArrayList;

/**
 * A class can implement the CardManagerObserver interface when it wants to be informed of changes in observable objects.
 */
public interface CardManagerObserver {

    /**
     * Get an update from the CardManager when a card is added in a card slot.
     * @param indexCardSlot the index of the card slot.
     * @param rowDeckDevelopment the row of the card in the deck development.
     * @param colDeckDevelopment the column of the card in the deck development.
     */
    void cardSlotUpdate(int indexCardSlot, int rowDeckDevelopment, int colDeckDevelopment);

    /**
     * Get an update from the CardManager when a leader is activated.
     * @param leaders the ArrayList of leaders
     */
    void leaderActivated(ArrayList<Leader> leaders);

    /**
     * Get an update from the CardManager when a leader is discarded.
     * @param leaderIndex the index of the leader discarded.
     */
    void leaderDiscard(int leaderIndex);
}
