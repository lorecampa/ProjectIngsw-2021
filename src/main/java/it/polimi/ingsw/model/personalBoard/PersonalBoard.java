package it.polimi.ingsw.model.personalBoard;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.faithTrack.FaithTrack;

import java.io.File;
import java.io.IOException;

public class PersonalBoard {
    private final FaithTrack faithTrack;
    private  CardManager cardManager;


    public PersonalBoard() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        this.faithTrack = mapper.readValue(new File("src/main/resources/json/FaithTrack.json"), FaithTrack.class);
        this.cardManager = new CardManager();

    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public CardManager getCardManager(){
        return cardManager;
    }


}
