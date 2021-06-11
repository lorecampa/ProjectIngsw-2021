package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.resource.Resource;

import java.util.ArrayList;

public interface CardManagerObserver {
    void cardSlotUpdate(int indexCardSlot, int rowDeckDevelopment, int colDeckDevelopment);

    void leaderActivated(ArrayList<Leader> leaders);
    void leaderDiscard(int leaderIndex);

}
