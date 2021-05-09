package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.resource.Resource;

import java.util.ArrayList;

public interface ResourceManagerObserver {


    void bufferUpdate(ArrayList<Resource> resources);

    void anyConversionRequest(ArrayList<Resource> optionOfConversion,
                              ArrayList<Resource> optionOfDiscount,
                              int numOfAny, boolean isProduction);

}
