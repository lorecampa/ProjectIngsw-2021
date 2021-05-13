package it.polimi.ingsw.observer;


public interface ModelObserver {
    void currentPlayerChange(String nextPlayer);
    void removeDeckDevelopmentSinglePlayer(int row, int column);
}
