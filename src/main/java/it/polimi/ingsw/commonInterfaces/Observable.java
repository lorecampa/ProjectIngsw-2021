package it.polimi.ingsw.commonInterfaces;

public interface Observable {
    /**
     * Method to add observer in the observers array
     * @param observer is the observer to add
     */
    void attachObserver(Observer observer);

    /**
     * Method to call the update method of all observers
     */
    void notifyAllObservers();
}
