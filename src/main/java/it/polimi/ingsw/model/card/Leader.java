package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.activationEffect.OnActivationEffect;
import it.polimi.ingsw.model.card.creationEffect.OnCreationEffect;
import it.polimi.ingsw.model.card.requirement.Requirement;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

import java.util.ArrayList;

public class Leader extends Card{
    private ArrayList<OnCreationEffect> onCreationEffects;
    private boolean isActive;

    public Leader(int victoryPoints, int owner,
                  ArrayList<Requirement> requirements,
                  ArrayList<OnActivationEffect> onActivationEffects,
                  ArrayList<OnCreationEffect> onCreationEffects, boolean isActive) {

        super(victoryPoints, owner, requirements, onActivationEffects);
        this.onCreationEffects = onCreationEffects;
        this.isActive = isActive;
    }


    @Override
    public void setResourceManager(ResourceManager resourceManager) {
        super.setResourceManager(resourceManager);
        for (OnCreationEffect effect: onCreationEffects){
            effect.attachResourceManager(resourceManager);
        }
    }


    public void doOnCreationEffects(ResourceManager resourceManager){
        for(OnCreationEffect effect: onCreationEffects){
            effect.doCreationEffect(resourceManager);
        }
    }



}
