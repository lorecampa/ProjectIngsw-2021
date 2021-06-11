package it.polimi.ingsw.observer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.polimi.ingsw.exception.InvalidStateActionException;
import it.polimi.ingsw.message.clientMessage.ErrorType;
import it.polimi.ingsw.model.PlayerState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class GameMasterObservable {
    @JsonIgnore
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
    public void notifyGameMaster(Consumer<GameMasterObserver> consumer){
        gameMasterObserverList.forEach(consumer);

    }

    public void checkPlayerState(PlayerState... states) throws InvalidStateActionException {
        for(GameMasterObserver gm: gameMasterObserverList){
            if(!gm.isPlayerInState(states)){
                throw new InvalidStateActionException();
            }
        }
    }
}
