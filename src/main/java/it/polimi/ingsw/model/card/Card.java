package it.polimi.ingsw.model.card;


import it.polimi.ingsw.model.card.activationEffect.OnActivationEffect;
import it.polimi.ingsw.model.card.requirement.Requirement;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

import java.util.ArrayList;

public class Card {
    private int victoryPoints;
    private int owner;
    private ArrayList<Requirement> requirements;
    private ArrayList<OnActivationEffect> onActivationEffects;

    public Card(int victoryPoints, int owner, ArrayList<Requirement> requirements,
                ArrayList<OnActivationEffect> onActivationEffects) {

        this.victoryPoints = victoryPoints;
        this.owner = owner;
        this.requirements = requirements;
        this.onActivationEffects = onActivationEffects;
    }

    public boolean checkRequirements(){
        for(Requirement req: requirements){
            if (!req.checkRequirement()) return false;
        }
        return true;
    }

    public  void doActivationEffects(){
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

    public int getOwner() {
        return owner;
    }
}
