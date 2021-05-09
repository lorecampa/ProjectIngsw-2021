package it.polimi.ingsw.observer;


public interface ModelObserver {
    void currentPlayerChange();
    void removeDeckDevelopmentSinglePlayer(int row, int column);
}
