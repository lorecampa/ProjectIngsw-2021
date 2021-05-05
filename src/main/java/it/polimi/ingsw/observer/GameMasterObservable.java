package it.polimi.ingsw.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class GameMasterObservable {
    List<GameMasterObserver> gameMasterObserverList = new ArrayList<>();

    /**
     * Method to add observer in the observers array
     * @param observer is the observer to add
     */
    public void attachGameMasterObserver(GameMasterObserver observer){
        gameMasterObserverList.add(observer);
    }

    /**
     * Method to call the update method of all observers
     */
    public void notifyGameMasterObserver(Consumer<GameMasterObserver> consumer){
        gameMasterObserverList.forEach(consumer);
    }
}
