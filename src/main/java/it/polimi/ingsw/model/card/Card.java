package it.polimi.ingsw.model.card;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import it.polimi.ingsw.exception.CantMakeProductionException;
import it.polimi.ingsw.model.card.activationEffect.OnActivationEffect;
import it.polimi.ingsw.model.card.requirement.Requirement;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Class Card is an abstract class that defines the model of a card
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")

@JsonSubTypes({
        @Type(value = Leader.class, name = "Leader"),
        @Type(value = Development.class, name = "Development")
})
public  abstract class Card {
    private final int victoryPoints;
    private final ArrayList<Requirement> requirements;
    private final ArrayList<OnActivationEffect> onActivationEffects;
    private String owner = null;

    /**
     * Constructor Card creates a new Card instance
     * @param victoryPoints of type int - the card victory points
     * @param requirements of type ArrayList - the card requirements
     * @param onActivationEffects of type ArrayList - the card effects of activation type
     */
    public Card(int victoryPoints, ArrayList<Requirement> requirements,
                ArrayList<OnActivationEffect> onActivationEffects) {
        this.victoryPoints = victoryPoints;
        this.requirements = requirements;
        this.onActivationEffects = onActivationEffects;

    }

    /**
     * Method checkRequirements checks if all requirement of the card are satisfied
     * @return boolean - true if all requirements are satisfied, otherwise false
     */
    public boolean checkRequirements(){
        for(Requirement req: requirements){
            if (!req.checkRequirement()) return false;
        }
        return true;
    }

    /**
     * Method doEffects does all the effect of type activation
     * @throws CantMakeProductionException when the player can't afford the production cost
     */
    public  void doEffects() throws  CantMakeProductionException {
        for (OnActivationEffect effect: onActivationEffects ){
            effect.doActivationEffect();
        }
    }

    /**
     * Method setResourceManager attach the resource manager to all the activation effect
     * @param resourceManager of type ResourceManager is an instance of the resource manager of the player
     */
    public void setResourceManager(ResourceManager resourceManager){
        for (OnActivationEffect effect: onActivationEffects){
            effect.attachResourceManager(resourceManager);
        }
    }

    /**
     * Method setMarket attach the market to all the activation effect
     * @param market of type Market is an instance of the market of the game
     */
    public void setMarket(Market market){
        for(OnActivationEffect effect: onActivationEffects){
            effect.attachMarket(market);
        }
    }

    /**
     * Method getVictoryPoints is a getter for the victory points of the card
     * @return int - the victory point of the card
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Method getOwner is a getter for the owner of the card
     * @return String - the owner of the card
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Method setOwner is a setter for the owner of the card
     * @param username of type String is the owner to be set
     */
    public void setOwner(String username) {this.owner = username; }


    @Override
    public String toString() {
        String x;
        if(this.getClass() == Leader.class){
            x = "Leader";
        }else{
            x = "Development";
        }
        x+= "\nvictoryPoints= " + victoryPoints;

        for(Requirement req: requirements){
            x += "\n"+req;

        }
        if(this.onActivationEffects != null){
            for(OnActivationEffect onActivationEffect: onActivationEffects){
                x += onActivationEffect;
                x += "\n";
            }
        }

        return x;
    }
}
