package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.personalBoard.resourceManager.Depot;
import it.polimi.ingsw.model.resource.Resource;

import java.util.ArrayList;

/**
 * A class can implement the ResourceManagerObserver interface when it wants to be
 * informed of changes in observable objects.
 */
public interface ResourceManagerObserver {

    /**
     * Get an update from the ResourceManager when the resource buffer is changed.
     * @param resources the resource buffer.
     */
    void bufferUpdate(ArrayList<Resource> resources);

    /**
     * Get an update from the ResourceManager when it gets resource from market.
     * @param resources the resources from market.
     */
    void depotPositioningRequest(ArrayList<Resource> resources);

    /**
     * Get an update from the ResourceManager when some resources need to be removed from the warehouse.
     * @param resources the resources to remove.
     */
    void warehouseRemovingRequest(ArrayList<Resource> resources);

    /**
     * Get an update from the ResourceManager when there's a ANY resource to convert from cost.
     * @param optionOfConversion the option that the player has to convert.
     * @param optionOfDiscount the discount available to the player.
     * @param numOfAny the number of ANY to convert.
     */
    void anyRequirementConversionRequest(ArrayList<Resource> optionOfConversion,
                                         ArrayList<Resource> optionOfDiscount,
                                         int numOfAny);

    /**
     * Get an update from the ResourceManager when there's ANY resource to convert from profit.
     * @param numOfAny the number of ANY to convert.
     */
    void anyProductionProfitRequest(int numOfAny);

    /**
     * Get an update from the ResourceManager when the strongbox is changed.
     * @param strongboxUpdated the updated strongbox.
     */
    void strongboxUpdate(ArrayList<Resource> strongboxUpdated);

    /**
     * Get an update from the ResourceManager when a depot is changed.
     * @param depotUpdated the updated depot.
     * @param index the index of the depot.
     * @param isNormalDepot true if it's not a leader depot.
     */
    void depotUpdate(Resource depotUpdated, int index, boolean isNormalDepot);

    /**
     * Get an update from the ResourceManager when a leader depot is changed.
     * @param depots the depots of the leader.
     * @param isDiscard true if the depot is discarded.
     */
    void updateLeaderDepot(ArrayList<Depot> depots, boolean isDiscard);

    /**
     * Get an update from the ResourceManager when a card has been chosen correctly for production.
     */
    void productionCardSelectionCompleted();
}
