package it.polimi.ingsw.model.card.activationEffect;

import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

public interface OnActivationEffect {
    void doActivationEffect();
    void attachMarket(Market market);
    void attachResourceManager(ResourceManager resourceManager);
}
