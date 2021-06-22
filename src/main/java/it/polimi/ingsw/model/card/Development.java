package it.polimi.ingsw.model.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.CardDevData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.NotEnoughRequirementException;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.card.requirement.Requirement;
import java.util.ArrayList;


/**
 * Class Development is a class that represent all card of type development. It extends Card adding
 * all the possibility.
 */
public class Development extends  Card{
    private final int level;
    private final Color color;


    /**
     * Construct a Development Card with specific attributes.
     * @param victoryPoints the development card victory points.
     * @param requirements the development card requirements.
     * @param onActivationEffects the development card effects of type activation.
     * @param level the development card level.
     * @param color the development card color.
     */
    @JsonCreator
    public Development(@JsonProperty("id") int id,
                       @JsonProperty("victoryPoints") int victoryPoints,
                       @JsonProperty("requirements") ArrayList<Requirement> requirements,
                       @JsonProperty("onActivationEffects") ArrayList<Effect> onActivationEffects,
                       @JsonProperty("onCreationEffects") ArrayList<Effect> onCreationEffects,
                       @JsonProperty("level") int level,
                       @JsonProperty("color") Color color) {
        super(id, victoryPoints, requirements, onActivationEffects, onCreationEffects);
        this.level = level;
        this.color = color;
    }

    /**
     * Return the card's level.
     * @return the card's level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Return the card color.
     * @return the card color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Return a CardDevData based on the card's attributes.
     * @return a CardDevData based on the card's attributes.
     */
    public CardDevData toCardDevData(){
        ArrayList<ResourceData> resReq = new ArrayList<>();
        for (Requirement requirement: requirements){
            if (requirement.toResourceData() != null)
                resReq.addAll(requirement.toResourceData());
        }
        return new CardDevData(getId(),level,getVictoryPoints(),color.toColorData(), resReq, effectToClient());
    }


    /**
     * See {@link Card#checkRequirements()}.
     */
    public void checkRequirements() throws NotEnoughRequirementException {
        for(Requirement req: requirements){
            req.checkRequirement(true);
        }
    }

    /**
     * Return a String that describe the card.
     * @return a String that describe the card.
     */
    @Override
    public String toString() {
        String x = super.toString();
        x += "level= " + level;
        x+= "\ncolor= " + color.getDisplayName()+ "\n";
        return x;
    }
}
