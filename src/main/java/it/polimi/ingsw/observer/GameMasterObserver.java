package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.PlayerState;

/**
 * A class can implement the GameMasterObserver interface when it wants to be informed of changes in observable objects.
 */
public interface GameMasterObserver {
    /**
     * Get an update from the Faith Track when a vatican report is reached.
     * @param idVR the id of the vatican report reached.
     */
    void vaticanReportReached(int idVR);

    /**
     * Get an update from the Card Manager when a leader is discarded.
     */
    void discardLeader();

    /**
     * Get an update from Game Master when the winning condition is reached.
     */
    void winningCondition();

    /**
     * Get an update from the Resource Manager when resources are discarded.
     * @param numResources the number of discarded resources.
     */
    void discardResources(int numResources);

    /**
     * Get an update from the Faith Track when a player receive faith.
     * @param faithPoints the number of faith.
     */
    void increasePlayerFaithPoint(int faithPoints);

    /**
     * Get an update from the Card Manager when a development card is removed.
     * @param row the row of the removed card.
     * @param col the column of the removed card.
     */
    void onDeckDevelopmentCardRemove(int row, int col);

    /**
     * Get an update when the player state is changed.
     * @param playerState the new player state.
     */
    void onPlayerStateChange(PlayerState playerState);

    /**
     * Return true if the player is in any of the states in state.
     * @param state the states to check.
     * @return true if the player is in any of the states in state.
     */
    boolean isPlayerInState(PlayerState... state);
}
