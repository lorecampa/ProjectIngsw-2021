package it.polimi.ingsw.model.card;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import it.polimi.ingsw.client.data.EffectData;
import it.polimi.ingsw.exception.NotEnoughRequirementException;
import it.polimi.ingsw.model.PlayerState;
import it.polimi.ingsw.model.card.Effect.Effect;
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
 * Class Card is an abstract class that defines the model of a card.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")

@JsonSubTypes({
        @Type(value = Leader.class, name = "Leader"),
        @Type(value = Development.class, name = "Development")
})
public  abstract class Card {
    private final int id;
    private final int victoryPoints;
    final ArrayList<Requirement> requirements;
    private final ArrayList<Effect> onCreationEffect;
    private final ArrayList<Effect> onActivationEffects;
    private String owner = null;

    /**
     * Construct a Card with those specific attributes.
     * @param victoryPoints the card victory points.
     * @param requirements the card requirements.
     * @param onCreationEffect the card effects of type creation.
     * @param onActivationEffects the card effects of type activation.
     */
    public Card(int id,
                int victoryPoints,
                ArrayList<Requirement> requirements,
                ArrayList<Effect> onActivationEffects,
                ArrayList<Effect> onCreationEffect) {
        this.id = id;
        this.victoryPoints = victoryPoints;
        this.requirements = requirements;
        this.onActivationEffects = onActivationEffects;
        this.onCreationEffect = onCreationEffect;

    }

    /**
     * Checks if all requirements of the card are satisfied, leaders discounts are considered in the counting.
     * @throws NotEnoughRequirementException if the player doesn't have enough card or resources to satisfy all requirements.
     */
    public abstract void checkRequirements() throws NotEnoughRequirementException;


    /**
     * Activate all Creation effects.
     * @throws NotEnoughRequirementException if the player doesn't have enough card or resources to satisfy
     * all requirements of some effect.
     */
    public void  doCreationEffects() throws NotEnoughRequirementException {
        for(Effect effect: onCreationEffect) {
            effect.doEffect(PlayerState.LEADER_MANAGE_BEFORE);
        }
    }

    /**
     * Activate all Activation effects.
     * @param playerState the current state of the player.
     * @throws NotEnoughRequirementException if the player doesn't have enough card or resources to satisfy
     * all requirements of some effect.
     */
    public  void doActivationEffects(PlayerState playerState) throws NotEnoughRequirementException {
        for (Effect effect: onActivationEffects){
            effect.doEffect(playerState);
        }
    }

    /**
     * Attach all the player Managers and the Market so that the card will be able to perform its effects.
     * @param personalBoard the player's Personal Board.
     * @param market the game's Market.
     */
    public void attachCardToUser(PersonalBoard personalBoard, Market market){
        this.setResourceManager(personalBoard.getResourceManager());
        this.setCardManager(personalBoard.getCardManager());
        this.setMarket(market);
        this.setOwner(personalBoard.getUsername());
    }

    /**
     * Attach the resource manager to all the activation effect.
     * @param resourceManager the player's Resource Manager.
     */
    public void setResourceManager(ResourceManager resourceManager){
        onActivationEffects.forEach(x -> x.attachResourceManager(resourceManager));
        onCreationEffect.forEach(x -> x.attachResourceManager(resourceManager));
        requirements.forEach(x -> x.attachResourceManager(resourceManager));
    }

    /**
     * Attach the card manager to all the requirements.
     * @param cardManager the player's Card Manager.
     */
    private void setCardManager(CardManager cardManager){
        requirements.forEach(x -> x.attachCardManager(cardManager));
    }

    /**
     * Attach the market to all the activation effect
     * @param market the game's Market.
     */
    private void setMarket(Market market){
        onActivationEffects.forEach(x -> x.attachMarket(market));

    }

    /**
     * Return the card's victory points.
     * @return the card's victory points.
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Return the username of the card's owner.
     * @return the username of the card's owner.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Set the username of the card's owner.
     * @param username the username of the card's owner.
     */
    public void setOwner(String username) {this.owner = username; }

    /**
     * Return the card's id.
     * @return the card's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Return an ArrayList with all the card's Activation Effects.
     * @return an ArrayList with all the card's Activation Effects.
     */
    public ArrayList<Effect> getOnActivationEffects() {
        return onActivationEffects;
    }

    /**
     * Return an ArrayList with all the card's Creation Effects.
     * @return an ArrayList with all the card's Creation Effects.
     */
    public ArrayList<Effect> getOnCreationEffect() {
        return onCreationEffect;
    }

    /**
     * Return an ArrayList of EffectData based on the card attributes.
     * @return an ArrayList of EffectData based on the card attributes.
     */
    public ArrayList<EffectData> effectToClient(){
        ArrayList<EffectData> effects = new ArrayList<>();
        if (onActivationEffects != null)
            effects.addAll(onActivationEffects.stream().map(Effect::toEffectData).collect(Collectors.toCollection(ArrayList::new)));
        if (onCreationEffect != null)
            effects.addAll(onCreationEffect.stream().map(Effect::toEffectData).collect(Collectors.toCollection(ArrayList::new)));
        return effects;
    }

}
