package it.polimi.ingsw.model.card;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import it.polimi.ingsw.exception.CantMakeProductionException;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.card.Effect.State;
import it.polimi.ingsw.model.card.requirement.Requirement;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
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
    final ArrayList<Requirement> requirements;
    private final ArrayList<Effect> onCreationEffect;
    private final ArrayList<Effect> onActivationEffects;
    private String owner = null;

    /**
     * Constructor Card creates a new Card instance
     * @param victoryPoints of type int - the card victory points
     * @param requirements of type ArrayList - the card requirements
     * @param onCreationEffect of type ArrayList - the card effects of type creation
     * @param onActivationEffects of type ArrayList - the card effects of type activation
     */
    public Card(int victoryPoints,
                ArrayList<Requirement> requirements,
                ArrayList<Effect> onActivationEffects,
                ArrayList<Effect> onCreationEffect) {
        this.victoryPoints = victoryPoints;
        this.requirements = requirements;
        this.onActivationEffects = onActivationEffects;
        this.onCreationEffect = onCreationEffect;

    }

    /**
     * Method checkRequirements checks if all requirement of the card are satisfied
     * @return boolean - true if all requirements are satisfied, otherwise false
     */
    public abstract boolean checkRequirements();


    /**
     * Method doCreationEffect does all effect one time use when you buy the card for the first time
     * @throws CantMakeProductionException
     */
    public void  doCreationEffect() throws CantMakeProductionException {
        for(Effect effect: onCreationEffect) {
            effect.doEffect(State.CREATION_STATE);
        }
    }

    /**
     * Method doEffects does all the effect of type activation
     * @throws CantMakeProductionException when the player can't afford the production cost
     */
    public  void doEffects(State state) throws  CantMakeProductionException {
        for (Effect effect: onActivationEffects){
            effect.doEffect(state);
        }
    }

    /**
     * Method setResourceManager attach the resource manager to all the activation effect
     * @param resourceManager of type ResourceManager is an instance of the resource manager of the player
     */
    public void setResourceManager(ResourceManager resourceManager){
        for (Effect effect: onActivationEffects){
            effect.attachResourceManager(resourceManager);
        }
        for (Effect effect: onCreationEffect){
            effect.attachResourceManager(resourceManager);
        }
        for (Requirement requirement: requirements){
            requirement.attachResourceManager(resourceManager);
        }
    }

    public void setCardManager(CardManager cardManager){
        for(Requirement requirement: requirements){
            requirement.attachCardManager(cardManager);
        }
    }

    /**
     * Method setMarket attach the market to all the activation effect
     * @param market of type Market is an instance of the market of the game
     */
    public void setMarket(Market market){
        for(Effect effect: onActivationEffects){
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


        if (!onActivationEffects.isEmpty()){
            x += "\nOnActivationEffect:";
            for(Effect onActivationEffect: onActivationEffects){
                x += onActivationEffect;
                x += "\n";
            }
        }

        if(!onCreationEffect.isEmpty()){
            if (onActivationEffects.isEmpty()) x+="\n";
            x += "OnCreationEffect:";
            for(Effect onCreationEffect: onCreationEffect){
                x += onCreationEffect;
                x += "\n";
            }
        }

        return x;
    }
}
