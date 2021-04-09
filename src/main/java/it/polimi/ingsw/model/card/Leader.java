package it.polimi.ingsw.model.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.exception.CantMakeProductionException;
import it.polimi.ingsw.model.card.activationEffect.OnActivationEffect;
import it.polimi.ingsw.model.card.creationEffect.OnCreationEffect;
import it.polimi.ingsw.model.card.requirement.Requirement;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import java.util.ArrayList;

/**
 * Class Leader defines a class for all card of type leader. It extends Card class adding the
 * effect of type creation
 */
public class Leader extends Card{
    private final ArrayList<OnCreationEffect> onCreationEffects;
    private boolean active = false;

    /**
     * Constructor Leader creates a new Leader instance
     * @param victoryPoints of type int - the leader victory points
     * @param requirements of type ArrayList - the leader requirements
     * @param onActivationEffects of type ArrayList - the leader effects of type activation
     * @param onCreationEffects of type ArrayList - the leader effects of type creation
     */
    @JsonCreator
    public Leader(@JsonProperty("victoryPoints") int victoryPoints,
                  @JsonProperty("requirements") ArrayList<Requirement> requirements,
                  @JsonProperty("onActivationEffects") ArrayList<OnActivationEffect> onActivationEffects,
                  @JsonProperty("onCreationEffects") ArrayList<OnCreationEffect> onCreationEffects) {

        super(victoryPoints, requirements, onActivationEffects);
        this.onCreationEffects = onCreationEffects;
    }

    /**
     * Method doEffects defines a method that redefines the father method calling the doEffect of
     * the onCreationEffects as well if they are not already used
     * @throws CantMakeProductionException when the player can't afford the production cost
     */
    @Override
    public void doEffects() throws  CantMakeProductionException {
        super.doEffects();
        for(OnCreationEffect effect: onCreationEffects){
            if (!effect.isUsed()){
                effect.doCreationEffect();
            }
        }
    }


    /**
     * Method setResourceManager is a method that redefines the father method setting the resource manager
     * instance of the player even on the onCreationEffects
     * @param resourceManager of type ResourceManager is an instance of the resource manager of the player
     */
    @Override
    public void setResourceManager(ResourceManager resourceManager) {
        super.setResourceManager(resourceManager);
        for (OnCreationEffect effect: onCreationEffects){
            effect.attachResourceManager(resourceManager);
        }
    }

    /**
     * Method isActive is a getter method for knowing is the leader card has been already activated
     * by the player that owns it
     * @return boolean - true if leader card is active, otherwise false
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Method setActive is a setter method  for setting the attribute active to true when the player
     * decide to activate the leader card during the game
     * @param status of type boolean defines the new status of the leader card
     */
    public void setActive(boolean status) {
        this.active = status;
    }



    @Override
    public String toString() {
        String x = super.toString();
        if (this.onCreationEffects != null){
            for(OnCreationEffect onCreationEffect: onCreationEffects){
                x += onCreationEffect;
                x += "\n";
            }
        }

        return x;
    }
}
