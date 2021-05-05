package it.polimi.ingsw.model.personalBoard;

import it.polimi.ingsw.model.GameMaster;
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
    private final String username;


    public PersonalBoard(String username,
                         FaithTrack faithTrack,
                         ResourceManager resourceManager,
                         CardManager cardManager) throws IOException {

        this.username = username;
        this.faithTrack = faithTrack;
        this.resourceManager = resourceManager;

        this.cardManager = cardManager;
        this.inkwell=false;

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

    public String getUsername() {
        return username;
    }
}
