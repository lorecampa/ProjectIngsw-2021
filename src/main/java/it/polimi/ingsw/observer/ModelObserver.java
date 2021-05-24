package it.polimi.ingsw.observer;


import java.util.Map;
import java.util.TreeMap;

public interface ModelObserver {
    void currentPlayerChange(String nextPlayer);
    void removeDeckDevelopmentSinglePlayer(int row, int column);
    void weHaveAWinner(Map<Integer, String> matchRanking);
    void winningCondition();
    void invalidAction(String username);

}
