package it.polimi.ingsw.model.card;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import it.polimi.ingsw.client.data.EffectData;
import it.polimi.ingsw.exception.NotEnoughRequirementException;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.TurnState;
import it.polimi.ingsw.model.card.requirement.Requirement;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
     */
    public abstract void checkRequirements() throws NotEnoughRequirementException;


    /**
     * Method doCreationEffect does all effect one time use in onCreationEffect when you buy or activate
     * the card for the first time
     */
    public void  doCreationEffects() throws NotEnoughRequirementException {
        for(Effect effect: onCreationEffect) {
            effect.doEffect(TurnState.LEADER_MANAGE_BEFORE);
        }
    }

    public void discardCreationEffects(){
        onCreationEffect.forEach(Effect::discardEffect);
    }
    /**
     * Method doEffects does all the effect of type activation
     */
    public  void doEffects(TurnState turnState) throws NotEnoughRequirementException {
        for (Effect effect: onActivationEffects){
            effect.doEffect(turnState);
        }
    }


    public void attachCardToUser(PersonalBoard personalBoard, Market market){

        this.setResourceManager(personalBoard.getResourceManager());
        this.setCardManager(personalBoard.getCardManager());
        this.setMarket(market);
        this.setOwner(personalBoard.getUsername());
    }

    /**
     * Method setResourceManager attach the resource manager to all the activation effect
     * @param resourceManager of type ResourceManager is an instance of the resource manager of the player
     */
    public void setResourceManager(ResourceManager resourceManager){
        onActivationEffects.forEach(x -> x.attachResourceManager(resourceManager));
        onCreationEffect.forEach(x -> x.attachResourceManager(resourceManager));
        requirements.forEach(x -> x.attachResourceManager(resourceManager));
    }

    private void setCardManager(CardManager cardManager){
        requirements.forEach(x -> x.attachCardManager(cardManager));
    }

    /**
     * Method setMarket attach the market to all the activation effect
     * @param market of type Market is an instance of the market of the game
     */
    private void setMarket(Market market){
        onActivationEffects.forEach(x -> x.attachMarket(market));

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

    public ArrayList<Effect> getOnActivationEffects() {
        return onActivationEffects;
    }

    public ArrayList<Effect> getOnCreationEffect() {
        return onCreationEffect;
    }

    public ArrayList<EffectData> effectToClient(){
        ArrayList<EffectData> effects = new ArrayList<>();
        if (onActivationEffects != null)
            effects.addAll(onActivationEffects.stream().map(Effect::toEffectData).collect(Collectors.toCollection(ArrayList::new)));
        if (onCreationEffect != null)
            effects.addAll(onCreationEffect.stream().map(Effect::toEffectData).collect(Collectors.toCollection(ArrayList::new)));
        return effects;
    }

    @Override
    public String toString() {
        StringBuilder x = new StringBuilder("\n");
        if(this.getClass() == Leader.class){
            x.append("Leader");
        }else{
            x.append("Development");
        }
        x.append("\nvictoryPoints= ").append(victoryPoints);

        for(Requirement req: requirements){
            x.append("\n").append(req);

        }


        if (!onActivationEffects.isEmpty()){
            x.append("\nOnActivationEffect:");
            for(Effect onActivationEffect: onActivationEffects){
                x.append(onActivationEffect);
                x.append("\n");
            }
        }

        if(!onCreationEffect.isEmpty()){
            if (onActivationEffects.isEmpty()) x.append("\n");
            x.append("OnCreationEffect:");
            for(Effect onCreationEffect: onCreationEffect){
                x.append(onCreationEffect);
                x.append("\n");
            }
        }

        return x.toString();
    }
}
