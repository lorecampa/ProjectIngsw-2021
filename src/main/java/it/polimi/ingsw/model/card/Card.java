package it.polimi.ingsw.model.card;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.card.activationEffect.OnActivationEffect;
import it.polimi.ingsw.model.card.requirement.Requirement;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;



@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")

@JsonSubTypes({
        @Type(value = Leader.class, name = "Leader"),
        @Type(value = Development.class, name = "Development")
})

public  class Card {
    private int victoryPoints;
    private ArrayList<Requirement> requirements;
    private ArrayList<OnActivationEffect> onActivationEffects;
    private String owner = null;


    public boolean checkRequirements(){
        for(Requirement req: requirements){
            if (!req.checkRequirement()) return false;
        }
        return true;
    }


    public  void doEffects() throws NegativeResourceException {
        for (OnActivationEffect effect: onActivationEffects ){
            effect.doActivationEffect();
        }
    }


    public void setResourceManager(ResourceManager resourceManager){
        for (OnActivationEffect effect: onActivationEffects){
            effect.attachResourceManager(resourceManager);
        }
    }
    public void setMarket(Market market){
        for(OnActivationEffect effect: onActivationEffects){
            effect.attachMarket(market);
        }
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String username) {this.owner = username; }
}
