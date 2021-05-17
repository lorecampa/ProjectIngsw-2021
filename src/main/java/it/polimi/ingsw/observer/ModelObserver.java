package it.polimi.ingsw.observer;


import java.util.TreeMap;

public interface ModelObserver {
    void currentPlayerChange(String nextPlayer);
    void removeDeckDevelopmentSinglePlayer(int row, int column);
    void weHaveAWinner(TreeMap<Integer, String> players);
    void getWinningCondition(String user);
}
