package it.polimi.ingsw.commonInterfaces;

public interface Observable {
    void attachObserver(Observer observer);
    void notifyAllObservers();
}
