package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.message.ClientMessageHandler;

public class ActivateProductionMessage implements ClientMessage{
    private int cardSlot;
    private int indexCard;

    public ActivateProductionMessage(@JsonProperty("cardSlot") int cardSlot,
                                     @JsonProperty("indexCard") int indexCard) {
        this.cardSlot = cardSlot;
        this.indexCard = indexCard;
    }

    public int getCardSlot() {
        return cardSlot;
    }

    public int getIndexCard() {
        return indexCard;
    }


    @Override
    public void process(ClientMessageHandler handler) {
        handler.handleActivateProductionMessage(this);
    }


}
