package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.exception.NegativeResourceException;

public interface Marble {
    void doMarbleAction() throws NegativeResourceException;
}
