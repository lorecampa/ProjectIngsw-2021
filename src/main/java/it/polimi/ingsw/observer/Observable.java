package it.polimi.ingsw.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Observable<T> {

    private List<T> observers = new ArrayList<>();
    /**
     * Method to add observer in the observers array
     * @param observer is the observer to add
     */
    public void attachObserver(T observer){
        observers.add(observer);
    }

    /**
     * Method to call the update method of all observers
     */
    public void notifyAllObservers(Consumer<T> consumer){
        observers.forEach(consumer);
    }
}
