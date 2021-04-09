package it.polimi.ingsw.model.personalBoard;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;

import java.io.File;
import java.io.IOException;

public class PersonalBoard {
    private final FaithTrack faithTrack;
    private final CardManager cardManager;
    private final ResourceManager resourceManager;
    private boolean inkwell;
    private String nickname;


    public PersonalBoard(String nickname) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        this.faithTrack = mapper.readValue(new File("src/main/resources/json/FaithTrack.json"), FaithTrack.class);
        this.cardManager = new CardManager();
        this.resourceManager = new ResourceManager();
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
