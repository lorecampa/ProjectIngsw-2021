package it.polimi.ingsw.observer;

public interface ResourceManagerObserver {
    /**
     * Method to get an update from the Resource Manager
     * @param numResources is the number of discarded resources
     */
    void discardResources(int numResources);
}
