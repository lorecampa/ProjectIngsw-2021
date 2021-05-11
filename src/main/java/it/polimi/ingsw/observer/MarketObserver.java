package it.polimi.ingsw.observer;


import it.polimi.ingsw.model.personalBoard.market.Marble;

import java.util.ArrayList;

public interface MarketObserver {
    void marketTrayChange(ArrayList<ArrayList<Marble>> marketTray, Marble lastMarble);
}
