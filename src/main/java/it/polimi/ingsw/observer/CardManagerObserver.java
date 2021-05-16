package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.resource.Resource;

public interface CardManagerObserver {
    void cardSlotUpdate(int indexCardSlot, int rowDeckDevelopment, int colDeckDevelopment);
    void leaderActivated(Leader leader, int leaderIndex);
    void leaderDiscard(int leaderIndex);

}
