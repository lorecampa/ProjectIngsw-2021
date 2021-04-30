package it.polimi.ingsw.model.personalBoard;

import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import java.io.IOException;

public class PersonalBoard {
    private final FaithTrack faithTrack;
    private final CardManager cardManager;
    private final ResourceManager resourceManager;
    private boolean inkwell;
    private final String nickname;


    public PersonalBoard(String nickname, FaithTrack faithTrack, Development baseProduction) throws IOException {
        this.resourceManager = new ResourceManager();
        this.faithTrack = faithTrack;
        baseProduction.setResourceManager(getResourceManager());
        this.cardManager = new CardManager(baseProduction);
        this.inkwell=false;
        this.nickname=nickname;

    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public CardManager getCardManager(){
        return cardManager;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public boolean isInkwell() {
        return inkwell;
    }

    public void setInkwell(boolean inkwell) {
        this.inkwell = inkwell;
    }
}
