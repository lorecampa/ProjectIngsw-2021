package it.polimi.ingsw.observer;


import java.util.TreeMap;

public interface ModelObserver {
    void currentPlayerChange(String nextPlayer);
    void removeDeckDevelopmentSinglePlayer(int row, int column);
    void weHaveAWinner(TreeMap<Integer, String> matchRanking);
    void winningCondition(String user);
}
