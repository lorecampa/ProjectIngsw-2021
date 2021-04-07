package it.polimi.ingsw.model.card.creationEffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import java.util.ArrayList;

/**
 * Discount class defines represent all effect concerned the discount
 */
public class DiscountEffect implements OnCreationEffect {
    private final ArrayList<Resource> discounts;
    private  boolean used = false;
    private ResourceManager resourceManager = null;

    /**
     * Constructor DiscountEffect creates a new DiscountEffect instance
     * @param discounts of type ArrayList - the resources that will be discounted
     */
    @JsonCreator
    public DiscountEffect(@JsonProperty("discounts") ArrayList<Resource> discounts) {
        this.discounts = discounts;
    }

    /**
     * Method doCreationEffect is responsible to pass all the discounts to the resource manager and
     * then to change the value of used from false to true
     * @throws NegativeResourceException when resources in discounts contain negative values
     */
    @Override
    public void doCreationEffect() throws NegativeResourceException {
        for(Resource res: discounts){
            //clone the res and then add to it
            resourceManager.addDiscount(ResourceFactory.createResource(res.getType(), res.getValue()));
        }
        this.used = true;
    }

    /**
     * Method attachResourceManager attach the resource manager in order to use it
     * @param resourceManager of type ResourceManager is an instance of the resource manager
     */
    @Override
    public void attachResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    /**
     * Method isUsed return the status of the creation effect during the game
     * @return boolean - true if the card has been used, otherwise false
     */
    @Override
    public boolean isUsed() {
        return this.used;
    }


    @Override
    public String toString() {
        String x = "\ndiscounts= ";
        for (Resource res: discounts){
            x+= "{"+ res.getType().getDisplayName()+", "+res.getValue()+"}  ";
        }
        return x;
    }
}
