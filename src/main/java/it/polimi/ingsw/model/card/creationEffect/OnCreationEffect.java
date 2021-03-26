package it.polimi.ingsw.model.card.creationEffect;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

public interface OnCreationEffect {
    void doCreationEffect(ResourceManager resourceManager);
    void attachResourceManager(ResourceManager resourceManager);

}

