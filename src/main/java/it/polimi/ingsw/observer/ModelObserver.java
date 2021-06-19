package it.polimi.ingsw.observer;

import java.util.Map;

/**
 * A class can implement the ModelObserver interface when it wants to be informed of changes in observable objects.
 */
public interface ModelObserver {

    /**
     * Get an update from the GameMaster when the current player is changed.
     * @param nextPlayer the new player that has the turn.
     */
    void currentPlayerChange(String nextPlayer);

    /**
     * Get an update from the GameMaster when a development card is removed by a token.
     * @param row the row of the removed card.
     * @param column the column of the removed card.
     */
    void removeDeckDevelopmentSinglePlayer(int row, int column);

    /**
     * Get an update from the GameMaster when the game is finished and a winner is chosen.
     * @param matchRanking the leaderboard of the match.
     */
    void weHaveAWinner(Map<Float, String> matchRanking);

    /**
     * Get an update from the GameMaster when the winning position is reached.
     */
    void winningCondition();
}
