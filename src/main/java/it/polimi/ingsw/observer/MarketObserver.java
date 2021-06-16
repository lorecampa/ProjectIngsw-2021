package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.personalBoard.market.Marble;

import java.util.ArrayList;

/**
 * A class can implement the MarketObserver interface when it wants to be informed of changes in observable objects.
 */
public interface MarketObserver {
    /**
     * Get an update from the market when the market tray is changed.
     * @param marketTray the marketTray of the market.
     * @param lastMarble the new marble to insert.
     */
    void marketTrayChange(ArrayList<ArrayList<Marble>> marketTray, Marble lastMarble);
}
