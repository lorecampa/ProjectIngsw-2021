package it.polimi.ingsw.observer;

public interface CardManagerObserver {
    void cardSlotUpdate(int indexCardSlot, int rowDeckDevelopment, int colDeckDevelopment);
    void leaderManage(int leaderIndex, boolean discard);
}
