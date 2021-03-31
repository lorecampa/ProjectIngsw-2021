package it.polimi.ingsw.model.card;

import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.card.creationEffect.OnCreationEffect;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import java.util.ArrayList;

public class Leader extends Card{
    private ArrayList<OnCreationEffect> onCreationEffects;
    private boolean active = false;

    //set the resource manager in the OnActivationEffects(super) and in the
    //onCreationEffects
    @Override
    public void setResourceManager(ResourceManager resourceManager) {
        super.setResourceManager(resourceManager);
        for (OnCreationEffect effect: onCreationEffects){
            effect.attachResourceManager(resourceManager);
        }
    }


    @Override
    public void doEffects() throws NegativeResourceException {
        super.doEffects();
        for(OnCreationEffect effect: onCreationEffects){
            if (!effect.isUsed()){
                effect.doCreationEffect();
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
