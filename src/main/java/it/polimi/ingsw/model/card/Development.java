package it.polimi.ingsw.model.card;

import com.fasterxml.jackson.annotation.JsonTypeName;
import it.polimi.ingsw.model.card.activationEffect.OnActivationEffect;
import it.polimi.ingsw.model.card.requirement.Requirement;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

import java.util.ArrayList;

public class Development extends  Card{

    private int level;
    private Color color;
    private ResourceManager resourceManager = null;


    public void buyCard(){
        //TODO
        //remove resources from strongbox and warehouse

    }

    @Override
    public void setResourceManager(ResourceManager resourceManager) {
        super.setResourceManager(resourceManager);
        this.resourceManager = resourceManager;

    }
}
