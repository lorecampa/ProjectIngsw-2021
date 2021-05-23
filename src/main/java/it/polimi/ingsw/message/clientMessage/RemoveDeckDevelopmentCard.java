package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.CLIMessageHandler;
import it.polimi.ingsw.client.ClientMessageHandler;

public class RemoveDeckDevelopmentCard implements ClientMessage{
    private final int row;
    private final int column;

    @JsonCreator
    public RemoveDeckDevelopmentCard(@JsonProperty("row") int row,
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
        handler.handleDeckDevCardRemoving(this);
    }
}
