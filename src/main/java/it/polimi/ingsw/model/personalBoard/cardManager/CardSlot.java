package it.polimi.ingsw.model.personalBoard.cardManager;

import it.polimi.ingsw.client.data.CardDevData;
import it.polimi.ingsw.exception.CardWithHigherOrSameLevelAlreadyIn;
import it.polimi.ingsw.model.card.Development;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CardSlot {
    private final ArrayList<Development> developments = new ArrayList<>();
    private Development buffer = null;
    /**
     * Method to get the higher lv present in this slot
     */
    public int getLvReached() {
        return developments.size();
    }

    /**
     * Method to insert a card in CardSlot
     * @param newCard is the card I want to insert
     * @throws  CardWithHigherOrSameLevelAlreadyIn if the card lv is already in the CardSlot
     */
    public void insertCard(Development newCard) throws CardWithHigherOrSameLevelAlreadyIn {
        if(developments.size() + 1 != newCard.getLevel()){
            throw new CardWithHigherOrSameLevelAlreadyIn("This card slot already contain that lv or higher!");
        }
        buffer= newCard;
    }

    public void emptyBuffer(){
        if (buffer != null){
            developments.add(buffer);
            buffer = null;
        }
    }

    public ArrayList<CardDevData> toCardSlotData(){
        return developments.stream().map(Development::toCardDevData).collect(Collectors.toCollection(ArrayList::new));
    }

    public Development getDevelopment(int index) {
        return developments.get(index);
    }

    public Development getLastInsertedCard(){
        return developments.get(getLvReached() - 1);
    }

    public int getVictoryPoint(){
        return developments.stream().mapToInt(Development::getVictoryPoints).sum();
    }
}
