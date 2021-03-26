package it.polimi.ingsw.model.card.creationEffect;

import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import java.util.ArrayList;

public class WarehouseEffect  implements OnCreationEffect {
    private ResourceManager resourceManager;
    private ArrayList<Resource> depots;

    public WarehouseEffect(ArrayList<Resource> depots) {
        this.depots = depots;
        this.resourceManager = null;
    }

    @Override
    public void doCreationEffect(ResourceManager resourceManager) {
        //TODO
        //add a depot in warehouse slot
    }

    @Override
    public void attachResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
}
