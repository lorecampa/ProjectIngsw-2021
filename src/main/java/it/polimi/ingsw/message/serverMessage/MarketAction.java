package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.*;
import it.polimi.ingsw.server.ServerMessageHandler;

public class MarketAction implements ServerMessage{
    private final int selection;
    private final boolean row;

    @JsonCreator
    public MarketAction(@JsonProperty("selection") int selection,
                        @JsonProperty("row") boolean row) {
        this.selection = selection;
        this.row = row;
    }

    public int getSelection() {
        return selection;
    }

    public boolean isRow() {
        return row;
    }


    @Override
    public void process(ServerMessageHandler handler) {
        handler.handleMarketAction(this);

        handler.getVirtualClient().ifPresent(x->x.addToLog(this));
    }

    @Override
    public String toString() {
        return " - Market Action";
    }
}
