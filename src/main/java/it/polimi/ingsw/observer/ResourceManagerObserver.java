package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.personalBoard.resourceManager.Depot;
import it.polimi.ingsw.model.resource.Resource;

import java.util.ArrayList;

public interface ResourceManagerObserver {


    void bufferUpdate(ArrayList<Resource> resources);

    void manageResourceRequest(ArrayList<Resource> resources, boolean isFromMarket);



    void anyRequirementConversionRequest(ArrayList<Resource> optionOfConversion,
                                         ArrayList<Resource> optionOfDiscount,
                                         int numOfAny);

    void anyProductionProfitRequest(int numOfAny);

    void strongboxUpdate(ArrayList<Resource> strongboxUpdated);

    void depotUpdate(Resource depotUpdated, int index, boolean isNormalDepot);

    void updateLeaderDepot(ArrayList<Depot> depots, boolean isDiscard);
}
