package it.polimi.ingsw.model.card.creationEffect;

import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import java.util.ArrayList;

public class DiscountEffect implements OnCreationEffect {
    private ResourceManager resourceManager;
    private ArrayList<Resource> discounts;

    public DiscountEffect(ArrayList<Resource> discount) {
        this.resourceManager = null;
        this.discounts = discount;
    }


    @Override
    public void doCreationEffect(ResourceManager resourceManager) {
        //TODO
        //change resourceManager discount attribute in resource manager
    }

    @Override
    public void attachResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
}
