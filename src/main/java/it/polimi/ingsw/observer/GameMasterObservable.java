package it.polimi.ingsw.observer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.polimi.ingsw.exception.InvalidStateActionException;
import it.polimi.ingsw.model.PlayerState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class GameMasterObservable {
    @JsonIgnore
    List<GameMasterObserver> gameMasterObserverList = new ArrayList<>();

    /**
     * Add observer in the observers array.
     * @param observer the observer to add.
     */
    public void attachGameMasterObserver(GameMasterObserver observer){
        gameMasterObserverList.add(observer);
    }

    /**
     * Call the update method of all observers.
     */
    public void notifyGameMaster(Consumer<GameMasterObserver> consumer){
        gameMasterObserverList.forEach(consumer);
    }

    /**
     * Check if the player's state is any of states.
     * @param states the states to check.
     * @throws InvalidStateActionException if the player is not in any states.
     */
    public void checkPlayerState(PlayerState... states) throws InvalidStateActionException {
        for(GameMasterObserver gm: gameMasterObserverList){
            if(!gm.isPlayerInState(states)){
                throw new InvalidStateActionException();
            }
        }
    }
}
