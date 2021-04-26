package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.message.ClientMessageHandler;
import it.polimi.ingsw.message.Handler;

public class BuyDevelopmentCardMessage implements ClientMessage{
    private int row;
    private int column;

    @JsonCreator
    public BuyDevelopmentCardMessage(@JsonProperty("row") int row,
                                     @JsonProperty("column") int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }


    @Override
    public void process(ClientMessageHandler handler) {
        handler.handleBuyDevelopmentCard(this);
    }
}
