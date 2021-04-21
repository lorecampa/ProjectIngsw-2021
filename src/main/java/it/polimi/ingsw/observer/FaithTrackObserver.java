package it.polimi.ingsw.observer;

public interface FaithTrackObserver {
    /**
     * Method to get an update from the Faith Track
     * @param idVR is the id of the vatican report reached
     */
    void vaticanReportReached(int idVR);
}
