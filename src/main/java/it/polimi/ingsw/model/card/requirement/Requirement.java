package it.polimi.ingsw.model.card.requirement;

import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

public interface Requirement {
    boolean checkRequirement();
    void attachResourceManager(ResourceManager resourceManager);
    void cardManager(CardManager cardManager);
}
