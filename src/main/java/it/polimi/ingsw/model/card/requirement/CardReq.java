package it.polimi.ingsw.model.card.requirement;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;


public class CardReq implements Requirement {
    Color color;
    int level;
    int numRequired;
    CardManager cardManager = null;


    @Override
    public boolean checkRequirement() {
        return  true;
        //TODO
        //control cardsSlot and checks all development cards color and level
    }

    @Override
    public void attachResourceManager(ResourceManager resourceManager) {

    }

    @Override
    public void attachCardManager(CardManager cardManager) {
        this.cardManager = cardManager;
    }
}
