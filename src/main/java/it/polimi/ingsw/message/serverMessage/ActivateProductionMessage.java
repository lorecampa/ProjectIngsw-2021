package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.message.ClientMessageHandler;
import it.polimi.ingsw.message.ServerMessageHandler;
import it.polimi.ingsw.message.clientMessage.ClientMessage;


public class ActivateProductionMessage implements ServerMessage {
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
    public void process(ServerMessageHandler handler) {
        handler.handleActivateProductionMessage(this);
    }

}
