package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.resource.Resource;

import java.util.ArrayList;

public interface ResourceManagerObserver {

    void depotModify(Resource resource, int depotIndex, boolean isDepotLeader);

    void depotSwitch(int from, int to, boolean isToLeader);

    void strongboxModify(Resource resource, boolean isAdd);

    void anyConversionRequest(ArrayList<Resource> optionOfConversion,
                              ArrayList<Resource> optionOfDiscount,
                              int numOfAny, boolean isProduction);

}
