package it.polimi.ingsw.observer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface Observable<T> {
    /**
     * Method to add observer in the observers array
     * @param observer is the observer to add
     */
    void attachObserver(T observer);

    /**
     * Method to call the update method of all observers
     */
    void notifyAllObservers(Consumer<T> consumer);
}
