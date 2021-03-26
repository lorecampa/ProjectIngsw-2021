package it.polimi.ingsw.model.card.requirement;

import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

public class CardReq implements Requirement {
    Color color;
    CardManager cardManager;
    int level;
    int numRequired;

    public CardReq(Color color, int level, int numRequired) {
        this.color = color;
        this.cardManager = null;
        this.level = level;
        this.numRequired = numRequired;
    }


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
    public void cardManager(CardManager cardManager) {
        this.cardManager = cardManager;
    }
}
