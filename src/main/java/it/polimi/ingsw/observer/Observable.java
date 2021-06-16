package it.polimi.ingsw.observer;
import java.util.function.Consumer;

public interface Observable<T> {
    /**
     * Add observer in the observers array.
     * @param observer the observer to add.
     */
    void attachObserver(T observer);

    /**
     * Call the update method of all observers.
     */
    void notifyAllObservers(Consumer<T> consumer);
}
