package it.polimi.ingsw.observer;

import it.polimi.ingsw.message.clientMessage.ErrorType;

public interface ModelObserver {
    void currentPlayerChange();
    void removeDeckDevelopmentSinglePlayer(int row, int column);
}
