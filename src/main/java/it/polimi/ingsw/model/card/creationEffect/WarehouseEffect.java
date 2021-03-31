package it.polimi.ingsw.model.card.creationEffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import java.util.ArrayList;


public class WarehouseEffect  implements OnCreationEffect {
    private ArrayList<Resource> depots;
    private boolean used = false;
    private ResourceManager resourceManager = null;

    @Override
    public void doCreationEffect() {
        //TODO
        //add a depot in warehouse slot and then sets this.used = true
    }

    @Override
    public void attachResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }


    @Override
    public boolean isUsed() {
        return this.used;
    }
}

