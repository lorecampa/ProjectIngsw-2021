package it.polimi.ingsw.observer;

public interface CardManagerObserver {
    void cardSlotUpdate(int indexCardSlot, int rowDeckDevelopment, int colDeckDevelopment);
    void leaderStatusUpdate(int leaderIndex, boolean discard);
}
