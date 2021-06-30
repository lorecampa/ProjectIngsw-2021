package it.polimi.ingsw.message.clientMessage;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;

public class CardSlotUpdate implements ClientMessage{
    private final int rowDeckDevelopment;
    private final int colDeckDevelopment;
    private final int slotIndex;
    private final String username;

    @JsonCreator
    public CardSlotUpdate(@JsonProperty("rowDeckDevelopment")int rowDeckDevelopment,
                             @JsonProperty("colDeckDevelopment")int colDeckDevelopment,
                             @JsonProperty("slotIndex")int slotIndex,
                          @JsonProperty("username") String username) {
        this.rowDeckDevelopment = rowDeckDevelopment;
        this.colDeckDevelopment = colDeckDevelopment;
        this.slotIndex = slotIndex;
        this.username = username;
    }

    public int getRowDeckDevelopment() {
        return rowDeckDevelopment;
    }

    public int getColDeckDevelopment() {
        return colDeckDevelopment;
    }

    public int getSlotIndex() {
        return slotIndex;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.cardSlotUpdate(this);
    }
}
