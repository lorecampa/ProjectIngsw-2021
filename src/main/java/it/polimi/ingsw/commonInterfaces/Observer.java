package it.polimi.ingsw.commonInterfaces;

public interface Observer {
    /**
     * Method to get an update from the Faith Track
     * @param idVR is the id of the vatican report reached
     */
    void updateFromFaithTrack(int idVR);

    /**
     * Method to get an update from the Resource Manager
     * @param positions is the number of discarded resources
     */
    void updateFromResourceManager(int positions);

    /**
     * Method to get an update from the Card Manager
     */
    void updateFromCardManager();
}
