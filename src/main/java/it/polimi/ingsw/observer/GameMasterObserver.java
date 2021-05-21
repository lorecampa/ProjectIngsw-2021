package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.TurnState;

public interface GameMasterObserver {
    /**
     * Method to get an update from the Faith Track
     * @param idVR is the id of the vatican report reached
     */
    void vaticanReportReached(int idVR);

    /**
     * Method to get an update from the Card Manager
     */
    void discardLeader();

    void winningCondition();

    /**
     * Method to get an update from the Resource Manager
     * @param numResources is the number of discarded resources
     */
    void discardResources(int numResources);

    void increasePlayerFaithPoint(int faithPoints);

    void onDeckDevelopmentCardRemove(int row, int col);

    void onTurnStateChange(TurnState turnState);


}
