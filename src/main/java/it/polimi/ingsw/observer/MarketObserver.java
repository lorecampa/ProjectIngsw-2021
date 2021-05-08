package it.polimi.ingsw.observer;

import it.polimi.ingsw.client.data.ColorData;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.resource.Resource;

import java.util.ArrayList;

public interface MarketObserver {
    void marketTrayChange(ArrayList<ColorData> sequenceUpdated,
                          ColorData lastMarble,
                          int selection, boolean isRow);
}
