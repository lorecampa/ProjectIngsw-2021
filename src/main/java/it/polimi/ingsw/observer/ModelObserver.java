package it.polimi.ingsw.observer;

public interface ModelObserver extends FaithTrackObserver, CardManagerObserver, ResourceManagerObserver{


    void currentPlayerChange();


}
