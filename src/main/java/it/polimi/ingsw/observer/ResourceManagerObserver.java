package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.resource.Resource;

import java.util.ArrayList;

public interface ResourceManagerObserver {


    void bufferUpdate(ArrayList<Resource> resources);

    void manageResourceRequest(ArrayList<Resource> resources, boolean isFromMarket);

    void anyConversionRequest(ArrayList<Resource> optionOfConversion,
                              ArrayList<Resource> optionOfDiscount,
                              int numOfAny, boolean isProduction);

    void strongboxUpdate(ArrayList<Resource> strongboxUpdated);

    void depotUpdate(Resource depotUpdated, int index, boolean isNormalDepot);

}
